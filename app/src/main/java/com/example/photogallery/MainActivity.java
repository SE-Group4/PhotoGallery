package com.example.photogallery;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    public static final int SEARCH_REQUEST = 0;
    static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestStoragePermission();

        // take picture
        Button snapButton = (Button) findViewById(R.id.btnSnap);
        snapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                invokeCameraActivity(v);
            }
        });

        // open search window
        Button searchButton = (Button) findViewById(R.id.btnSearch);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSearchActivity(v);
            }
        });
    }

    /* helper methods */

    // request permission to store images
    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            // temporary fix to prevent crash if current Android version lower than required SDK
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{ Manifest.permission.WRITE_EXTERNAL_STORAGE }, 1);
            }
        }
    }

    // search button
    private void openSearchActivity(View v) {
        Intent intent = new Intent(this, SearchActivity.class);
        startActivityForResult(intent, SEARCH_REQUEST);
    }

    // snap button
    private void invokeCameraActivity(View v) {
        Intent intent = new Intent(this, CameraActivity.class);
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
    }
}