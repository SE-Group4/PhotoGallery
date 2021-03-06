package com.example.photogallery.Presenter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;

import static android.content.Context.LOCATION_SERVICE;

public class ImageAdapter extends BaseAdapter {

    private final Context context;
    private File[] images;
    static final double[] FALLBACK_COORDINATES = {49.220509, -123.007111};

    public ImageAdapter(Context context) {
        this.context = context;
        this.images = listImages();
    }

    public void updateImages() {
        this.images = listImages();
    }

    public int getCount() {
        return this.images.length;
    }

    public Object getItem(int position) {
        return this.images[position];
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(this.context);
            imageView.setLayoutParams(new GridView.LayoutParams(250, 250));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }
        Bitmap b = BitmapFactory.decodeFile(this.images[position].getAbsolutePath());
        Bitmap scaledB = Bitmap.createScaledBitmap(b, 200, 200, true);
        imageView.setImageBitmap(Bitmap.createBitmap(scaledB, 0, 0, scaledB.getWidth(), scaledB.getHeight()));
        scaledB.recycle();
        return imageView;
    }

    private File[] listImages() {
        File directory = new File(this.context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).toString());
        return directory.listFiles();
    }

    public void updateCoordinateTags() {
        double[] coordinates = getCoordinates();
        for (int i = 0; i < this.images.length; i++) {
            try {
                String path = images[i].getAbsolutePath();
                ExifInterface exifInterface = new ExifInterface(path);
                if (exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE) == null || exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE) == null) {
                    exifInterface.setAttribute(ExifInterface.TAG_GPS_LATITUDE, convertToGPS(coordinates[0]));
                    exifInterface.setAttribute(ExifInterface.TAG_GPS_LONGITUDE, convertToGPS(coordinates[1]));
                    exifInterface.saveAttributes();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public File[] filterImages(final Date startTimestamp, final Date endTimestamp, final String keywords, final String lat, final String lng) {
        File[] files = new File(this.context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).toString()).listFiles();
        files = Arrays.stream(files)
                .filter(f -> acceptByTimestamp(f, startTimestamp, endTimestamp) &&
                        acceptByKeywords(f, keywords) &&
                        acceptByCoordinates(f, lat, lng)
                ).toArray(File[]::new);
        this.images = files;
        return files;
    }

    private static boolean acceptByKeywords(File f, final String keywords) {
        return (keywords.equals("") || f.getPath().contains(keywords));
    }

    private static boolean acceptByTimestamp(File f, final Date startTimestamp, final Date endTimestamp) {
        return ((startTimestamp == null && endTimestamp == null) || (f.lastModified() >= startTimestamp.getTime() && f.lastModified() <= endTimestamp.getTime()));
    }

    private static boolean acceptByCoordinates(File f, String latitude, String longitude) {
        if (latitude.equals("") && longitude.equals("")) {
            return true;
        }

        String latTag = null;
        String lngTag = null;
        try {
            String imgPath = f.getAbsolutePath();
            final ExifInterface ei = new ExifInterface(imgPath);
            latTag = ei.getAttribute(ExifInterface.TAG_GPS_LATITUDE);
            lngTag = ei.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);

            double latD = Double.valueOf(latitude);
            double lngD = Double.valueOf(longitude);
            latitude = convertToGPS(latD);
            longitude = convertToGPS(lngD);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return (latTag.equals(latitude) && lngTag.equals(longitude));
    }

    // TODO: move out to a helper class
    static private final String convertToGPS(double coordinate) {
        StringBuilder sb = new StringBuilder(20);

        coordinate = Math.abs(coordinate);
        int degree = (int) coordinate;
        coordinate *= 60;
        coordinate -= (degree * 60.0d);
        int minute = (int) coordinate;
        coordinate *= 60;
        coordinate -= (minute * 60.0d);
        int second = (int) (coordinate * 1000.0d);

        sb.setLength(0);
        sb.append(degree);
        sb.append("/1,");
        sb.append(minute);
        sb.append("/1,");
        sb.append(second);
        sb.append("/1000");
        return sb.toString();
    }


    private double[] getCoordinates() {
        try {
            LocationManager locationManager = (LocationManager) context.getApplicationContext().getSystemService(LOCATION_SERVICE);
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            double[] coordinates = {0, 0};
            if (location != null) {
                coordinates[0] = location.getLatitude();
                coordinates[1] = location.getLongitude();
                return coordinates;
            }
        } catch (SecurityException e) {
            Log.e("getLocationFailed", e.getMessage());
        }
        return FALLBACK_COORDINATES;
    }

}