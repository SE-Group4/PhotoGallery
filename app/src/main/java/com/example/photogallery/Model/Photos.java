package com.example.photogallery.Model;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

public class Photos {
    private final Context context;
    private ArrayList<String> photoStringPaths;

    public Photos(Context context) {
        this.context = context;
        this.setPhotoStringPaths(new Date(Long.MIN_VALUE), new Date(), "");
    }

    public Context getContext() {
        return context;
    }

    public void setPhotoStringPaths(ArrayList<String> photoStringPaths) {
        this.photoStringPaths = photoStringPaths;
    }

    public void setPhotoStringPaths(Date startTimestamp, Date endTimestamp, String keywords) {
        File file = new File(context.getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).toString());
        photoStringPaths = new ArrayList<>();
        File[] fList = file.listFiles();
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
