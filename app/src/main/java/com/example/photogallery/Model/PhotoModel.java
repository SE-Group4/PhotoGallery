package com.example.photogallery.Model;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

public class PhotoModel {
    private final File downloadDirectory;
    private ArrayList<String> photoStringPaths;

    public PhotoModel(File downloadDirectory) {
        this.downloadDirectory = downloadDirectory;
        this.setPhotoStringPaths(new Date(Long.MIN_VALUE), new Date(), "");
    }

    public File[] getImagesList() {
        return downloadDirectory.listFiles();
    }

    public void setPhotoStringPaths(ArrayList<String> photoStringPaths) {
        this.photoStringPaths = photoStringPaths;
    }

    public void setPhotoStringPaths(Date startTimestamp, Date endTimestamp, String keywords) {
        photoStringPaths = new ArrayList<>();
        File[] fList = downloadDirectory.listFiles();
        if (fList != null) {
            for (File f : fList) {
                if (((startTimestamp == null && endTimestamp == null) || (f.lastModified() >= startTimestamp.getTime() && f.lastModified() <= endTimestamp.getTime())) && (keywords == "" || f.getPath().contains(keywords)))
                    photoStringPaths.add(f.getPath());
            }
        }
    }

    public ArrayList<String> getPhotoStringPaths() {
        return photoStringPaths;
    }
}
