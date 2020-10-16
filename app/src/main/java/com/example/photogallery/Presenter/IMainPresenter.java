package com.example.photogallery.Presenter;

import android.content.Intent;

public interface IMainPresenter {
    void handleRequestImageCapture(ImageAdapter imageAdapter);
    void handlePhotoPreviewUpdates(Intent data);
    void handleRequestSearchFilter(Intent data, ImageAdapter imageAdapter);
    void handleSearchFiltersCleared(ImageAdapter imageAdapter);
}
