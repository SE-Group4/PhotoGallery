package com.example.photogallery.Model;

import android.graphics.Bitmap;
import android.net.Uri;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

public class PhotoModel {

    private String caption;
    private Bitmap bitmap;

    public PhotoModel(String caption, Bitmap bitmap) {
        this.caption = "captionTest";
        this.bitmap = bitmap;
    }

    public void savePhoto() {

    }

    public void deletePhoto() {

    }
    public void setCaption(String caption) {
        this.caption = caption;
    }

    //Where should this go...
    private void fetchPhotos() {


    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getCaption() {
        return caption;
    }
}
