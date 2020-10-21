package com.example.photogallery.Contracts;

import com.example.photogallery.Model.PhotoModel;

import java.util.ArrayList;

public interface GalleryContract {
    public interface View {
        void onTakeSnap(android.view.View view);
        void onBack(android.view.View view);
    }


    public interface Presenter {
        void openCamera();
        void loadPhotos();
        void updatePhotos();
        void addPhoto(PhotoModel photo);
        void deletePhoto(PhotoModel photo);
        void goBack();
    }
}