package com.example.photogallery;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.io.File;

public class ImageAdapter extends BaseAdapter {

    private Context context;
    private File[] images;

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
        Bitmap scaledB = Bitmap.createScaledBitmap (b, 200, 200, true);

        imageView.setImageBitmap(Bitmap.createBitmap (scaledB, 0, 0, scaledB.getWidth(), scaledB.getHeight()));
        scaledB.recycle();

        return imageView;
    }

    // private helper methods

    // get list of images from downloads directory
    private File[] listImages() {
      File directory = new File(this.context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).toString());
      return directory.listFiles();
    }
}