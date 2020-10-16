package com.example.photogallery.View;

import android.view.View;

import com.example.photogallery.Presenter.ImageAdapter;

import java.io.IOException;

public interface MainView {
    void updateGridView(ImageAdapter imageAdapter);
    void openSearchActivity(View v);
    void takePhoto(View v) throws IOException;
}
