package com.example.photogallery;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.example.photogallery.Model.Photos;
import com.example.photogallery.Presenter.MainPresenter;
import com.example.photogallery.View.MainView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements MainView {
    public static final int SEARCH_ACTIVITY_REQUEST_CODE = 200;
    static final int REQUEST_IMAGE_CAPTURE = 33;
    public static final int REQUEST_PHOTO_PREVIEW = 100;

    private ImageAdapter imageAdapter;
    private GridView gv;
    private Button searchButton;
    private Photos photos;
    private MainPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        photos = new Photos(this);
        presenter = new MainPresenter(this, photos);
        imageAdapter = new ImageAdapter(this);

        // request permissions
        requestGPSPermission();

        // display image gallery
        updateGridView(imageAdapter);

        // open photo preview on Grid View item click
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                imageAdapter.updateImages();
                imageAdapter.notifyDataSetChanged();
                File img = (File) gv.getAdapter().getItem(position);

                // pass image link to the intent and start Photo Preview activity
                String imgPath = (String) img.getAbsolutePath();
                try {
                    final ExifInterface ei = new ExifInterface(imgPath);
                    Intent i = new Intent(getApplicationContext(), PhotoPreviewActivity.class);
                    i.putExtra("clickedImageTimestamp", "Timestamp: " + ei.getAttribute(ExifInterface.TAG_DATETIME));
                    i.putExtra("clickedImagePath", imgPath);
                    i.putExtra("photoPaths", photos.getPhotoStringPaths());
                    startActivityForResult(i, REQUEST_PHOTO_PREVIEW);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        // open search window
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSearchActivity(v);
            }
        });
    }

    public void initViews() {
        gv = (GridView) findViewById(R.id.gridView);
        searchButton = (Button) findViewById(R.id.btnSearch);
    }

    // update image list on result of Main Activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_PHOTO_PREVIEW && resultCode == RESULT_OK && data != null) {
            presenter.handlePhotoPreviewUpdates(data);
        }

        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            presenter.handleRequestImageCapture(imageAdapter);
        }

        if (requestCode == SEARCH_ACTIVITY_REQUEST_CODE) {
            if (resultCode == SearchActivity.FILTER_APPLIED) {
                presenter.handleRequestSearchFilter(data, imageAdapter);
            } else if (resultCode == SearchActivity.FILTER_CLEARED) {
                presenter.handleSearchFiltersCleared(imageAdapter);
            }
        }

    }

    @Override
    public void updateGridView(ImageAdapter imageAdapter) {
        gv.setAdapter(imageAdapter);
    }

    // referenced from activity_main.xml
    public void takePhoto(View v) throws IOException {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File downloadFolder = new File(getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).toString());
        if (!downloadFolder.exists()) downloadFolder.mkdir();
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "_caption_" + timeStamp + "_";
        File newPhoto = File.createTempFile(imageFileName, ".jpg", downloadFolder);
        if (newPhoto != null) {
            Uri photoURI = FileProvider.getUriForFile(this, "com.example.photogallery.fileprovider", newPhoto);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI); // FIXME: uncomment when saving images. NOTE: sets Bitmap data to null
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    // request permission to use GPS
    private void requestGPSPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, 222);
        }
    }

    // search button
    public void openSearchActivity(View v) {
        Intent intent = new Intent(this, SearchActivity.class);
        startActivityForResult(intent, SEARCH_ACTIVITY_REQUEST_CODE);
    }
}