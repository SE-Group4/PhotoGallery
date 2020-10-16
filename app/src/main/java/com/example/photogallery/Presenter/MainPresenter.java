package com.example.photogallery.Presenter;

import android.content.Intent;


import com.example.photogallery.Model.PhotoModel;
import com.example.photogallery.View.MainView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainPresenter implements IMainPresenter {

    public static final String PHOTO_PATHS = "photoPaths";

    private final MainView mv;
    private final PhotoModel photoModel;

    public MainPresenter(MainView mainView, PhotoModel photoModel) {
        this.mv = mainView;
        this.photoModel = photoModel;
    }

    /* Updates the model after image capture */
    @Override
    public void handleRequestImageCapture(ImageAdapter imageAdapter) {
        photoModel.setPhotoStringPaths(new Date(Long.MIN_VALUE), new Date(), "");
        imageAdapter.updateImages();
        imageAdapter.updateCoordinateTags();
        imageAdapter.notifyDataSetChanged();
    }

    @Override
    public void handlePhotoPreviewUpdates(Intent data) {
        photoModel.setPhotoStringPaths(data.getStringArrayListExtra(PHOTO_PATHS));
    }

    @Override
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
        photoModel.setPhotoStringPaths(startTimestamp, endTimestamp, keywords);
    }

    @Override
    public void handleSearchFiltersCleared(ImageAdapter imageAdapter) {
        imageAdapter.updateImages();
        imageAdapter.updateCoordinateTags();
        imageAdapter.notifyDataSetChanged();
        photoModel.setPhotoStringPaths(null, null, "");
    }

    @Override
    public ArrayList<String> getPhotoPaths(){
        return photoModel.getPhotoStringPaths();
    }
}
