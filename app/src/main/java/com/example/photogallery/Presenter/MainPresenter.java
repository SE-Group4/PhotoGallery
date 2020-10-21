package com.example.photogallery.Presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;


import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.photogallery.Contracts.MainContract;
import com.example.photogallery.Model.PhotoModel;
import com.example.photogallery.R;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainPresenter implements MainContract.Presenter {

    private Context context;

    public MainPresenter(Context context) {
        this.context = context;
    }

    public void handlePhotoPreviewUpdates(Intent data) {
        //photoModel.setPhotoStringPaths(data.getStringArrayListExtra(PHOTO_PATHS));
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
        //imageAdapter.filterImages(startTimestamp, endTimestamp, keywords, lat, lng);
        //imageAdapter.notifyDataSetChanged();
        //mv.updateRecycleView(imageAdapter);
        //photoModel.setPhotoStringPaths(startTimestamp, endTimestamp, keywords);
    }

    public void handleSearchFiltersCleared(ImageAdapter imageAdapter) {
        imageAdapter.updateImages();
        imageAdapter.updateCoordinateTags();
        imageAdapter.notifyDataSetChanged();
        //photoModel.setPhotoStringPaths(null, null, "");
    }

    public ArrayList<String> getPhotoPaths(){
        return null;
    }
}
