package com.example.photogallery.Presenter;

import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

import com.example.photogallery.ImageAdapter;
import com.example.photogallery.Model.Photos;
import com.example.photogallery.View.MainView;

import java.util.Date;

public class MainPresenter {

    private MainView mv;
    private Photos photos;

    public MainPresenter(MainView mainView, Photos photos) {
        this.mv = mainView;
        this.photos = photos;
    }

    public void handleRequestImageCaputre(ImageAdapter imageAdapter){
        photos.getPhotoList(new Date(Long.MIN_VALUE), new Date(), "");
        imageAdapter.updateImages();
        imageAdapter.updateCoordinateTags(getCoordinates());
        imageAdapter.notifyDataSetChanged();
    }

    private double[] getCoordinates() {
        try {
            LocationManager locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
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
