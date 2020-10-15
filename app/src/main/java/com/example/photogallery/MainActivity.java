package com.example.photogallery;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements MainView {
    public static final int SEARCH_ACTIVITY_REQUEST_CODE = 200;
    static final int REQUEST_IMAGE_CAPTURE = 33;
    static final double[] FALLBACK_COORDINATES = {49.220509, -123.007111};
    private ImageAdapter imageAdapter;
    private File newPhoto;
    private GridView gv;
    private Button searchButton;
    Photos photos = new Photos(getApplicationContext());
    MainPresenter presenter;
    public static final int mRequestCode = 100;
    public static final String PHOTO_PATHS = "photoPaths";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        presenter = new MainPresenter(this, photos);
        imageAdapter = new ImageAdapter(this);
        photos.getPhotoList(new Date(Long.MIN_VALUE), new Date(), "");

        // request permission to store pictures
        requestGPSPermission();

        // display image gallery
        gv.setAdapter(imageAdapter);

        // open photo preview on Grid View item click
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                File img = (File) gv.getAdapter().getItem(position);

                // pass image link to the intent and start Photo Preview activity
                String imgPath = (String) img.getAbsolutePath();
                try {
                    final ExifInterface ei = new ExifInterface(imgPath);
                    Intent i = new Intent(getApplicationContext(), PhotoPreviewActivity.class);
                    i.putExtra("clickedImageTimestamp", "Timestamp: " + ei.getAttribute(ExifInterface.TAG_DATETIME));
                    i.putExtra("clickedImagePath", imgPath);
                    i.putExtra("photoPaths", photos.getPhotoList());
                    startActivityForResult(i, mRequestCode);
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

    // update image list on resume of Main Activity
    @Override
    public void onResume() {
        super.onResume();
    }

    // update image list on result of Main Activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == mRequestCode && resultCode == RESULT_OK && data != null) {
            photos = data.getStringArrayListExtra(PHOTO_PATHS);
        }

        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            presenter.handleRequestImageCapture(imageAdapter);
        }

        if (requestCode == SEARCH_ACTIVITY_REQUEST_CODE) {
            if (resultCode == SearchActivity.FILTER_APPLIED) {
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
                imageAdapter.filterImages(startTimestamp, endTimestamp, keywords, lat, lng);
                imageAdapter.notifyDataSetChanged();
                gv.setAdapter(imageAdapter);
                photos = findPhotos(startTimestamp, endTimestamp, keywords);
                return;
            } else if (resultCode == SearchActivity.FILTER_CLEARED) {
                imageAdapter.updateImages();
                imageAdapter.updateCoordinateTags(getCoordinates());
                imageAdapter.notifyDataSetChanged();
                photos = findPhotos(null, null, "");
            }
        }

    }

    /* helper methods */

    // referenced from activity_main.xml
    public void takePhoto(View v) throws IOException {
        dispatchTakePictureIntent();
    }

    // request permission to store images
    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // temporary fix to prevent crash if current Android version lower than required SDK
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }
    }

    // request permission to use GPS
    private void requestGPSPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, 222);
        }
    }

    // invoke camera
    private void dispatchTakePictureIntent() throws IOException {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        newPhoto = createImageFile();

        if (newPhoto != null) {

            // get a URI for the file based on authority of app's own fileprovider
            Uri photoURI = FileProvider.getUriForFile(this, "com.example.photogallery.fileprovider", newPhoto);

            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI); // FIXME: uncomment when saving images. NOTE: sets Bitmap data to null
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    // create files/Download directory if it does not exist, then creates jpg file for new photo
    private File createImageFile() throws IOException {

        File downloadFolder = new File(getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).toString());

        // create the Download folder if it does not exist
        if (!downloadFolder.exists()) {
            downloadFolder.mkdir();
        }

        // generate image filename
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "_caption_" + timeStamp + "_";

        // prepares the final path for the output photo file
        File image = File.createTempFile(imageFileName, ".jpg", downloadFolder);

        return image;
    }

    // search button
    public void openSearchActivity(View v) {
        Intent intent = new Intent(this, SearchActivity.class);
        startActivityForResult(intent, SEARCH_ACTIVITY_REQUEST_CODE);
    }
}