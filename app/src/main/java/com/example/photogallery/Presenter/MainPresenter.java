package com.example.photogallery.Presenter;

import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

import com.example.photogallery.ImageAdapter;
import com.example.photogallery.Model.Photos;
import com.example.photogallery.View.MainView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.content.Context.LOCATION_SERVICE;

public class MainPresenter {

    public static final String PHOTO_PATHS = "photoPaths";
    static final double[] FALLBACK_COORDINATES = {49.220509, -123.007111};

    private MainView mv;
    private Photos photos;

    public MainPresenter(MainView mainView, Photos photos) {
        this.mv = mainView;
        this.photos = photos;
    }

    public void handleRequestImageCapture(ImageAdapter imageAdapter){
        photos.setPhotoStringPaths(new Date(Long.MIN_VALUE), new Date(), "");
        imageAdapter.updateImages();
        imageAdapter.updateCoordinateTags(getCoordinates());
        imageAdapter.notifyDataSetChanged();
    }

    public void handlePhotoPreviewUpdates(Intent data) {
        photos.setPhotoStringPaths(data.getStringArrayListExtra(PHOTO_PATHS));
    }

    public void handleRequestSearchFilter(Intent data, ImageAdapter imageAdapter) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date startTimestamp, endTimestamp;
        try {
            String from = data.getStringExtra("STARTTIMESTAMP");
            String to = data.getStringExtra("ENDTIMESTAMP");
            startTimestamp = format.parse(from);
            endTimestamp = format.parse(to);
        } catch (Exception ex) {
            startTimestamp = null;
            endTimestamp = null;
        }
        String keywords = data.getStringExtra("KEYWORDS");
        String lat = data.getStringExtra("LAT");
        String lng = data.getStringExtra("LNG");
        imageAdapter.filterImages(startTimestamp, endTimestamp, keywords, lat, lng);
        imageAdapter.notifyDataSetChanged();
        mv.updateGridView(imageAdapter);
        photos.setPhotoStringPaths(startTimestamp, endTimestamp, keywords);
        return;
    }

    public void handleSearchFiltersCleared(ImageAdapter imageAdapter) {
        imageAdapter.updateImages();
        imageAdapter.updateCoordinateTags(getCoordinates());
        imageAdapter.notifyDataSetChanged();
        photos.setPhotoStringPaths(null, null, "");
    }

    private double[] getCoordinates() {
        try {
            LocationManager locationManager = (LocationManager) photos.getContext().getApplicationContext().getSystemService(LOCATION_SERVICE);
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
