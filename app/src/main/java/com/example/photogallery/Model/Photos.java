package com.example.photogallery.Model;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

public class Photos {
    private Context context;

    public Photos(Context context) {
        this.context = context;
    }


    public ArrayList<String> getPhotoList(Date startTimestamp, Date endTimestamp, String keywords) {
        File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).toString());
        ArrayList<String> photos = new ArrayList<>();
        File[] fList = file.listFiles();
        if (fList != null) {
            for (File f : fList) {
                if (((startTimestamp == null && endTimestamp == null) || (f.lastModified() >= startTimestamp.getTime() && f.lastModified() <= endTimestamp.getTime())) && (keywords == "" || f.getPath().contains(keywords)))
                    photos.add(f.getPath());
            }
        }
        return photos;
    }
}
