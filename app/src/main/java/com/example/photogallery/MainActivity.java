package com.example.photogallery;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    public static final int SEARCH_REQUEST = 0;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private ImageAdapter imageAdapter;
    private File newPhoto;
    public static ArrayList<String> photos = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        photos = findPhotos();

        // request permission to store pictures
        requestStoragePermission();

        // display image gallery
        final GridView gv = (GridView) findViewById(R.id.gridView);
        imageAdapter = new ImageAdapter(this);
        gv.setAdapter(imageAdapter);

        // open photo preview on Grid View item click
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int positon, long id) {
                File img = (File) gv.getAdapter().getItem(positon);

                // pass image link to the intent and start Photo Preview activity
                String imgPath = (String) img.getAbsolutePath();
                try {
                    final ExifInterface ei = new ExifInterface(imgPath);
                    Intent i = new Intent(getApplicationContext(), PhotoPreviewActivity.class);
                    i.putExtra("clickedImageTimestamp", "Timestamp: " + ei.getAttribute(ExifInterface.TAG_DATETIME));
                    i.putExtra("clickedImagePath", imgPath);
                    startActivity(i);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
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

    // update image list on resume of Main Activity
    @Override
    public void onResume() {
        super.onResume();
        imageAdapter.updateImages();
        imageAdapter.notifyDataSetChanged();
    }

    // update image list on result of Main Activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_CANCELED) {
            newPhoto.delete();
        }

        imageAdapter.updateImages();
        imageAdapter.notifyDataSetChanged();
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
    private void openSearchActivity(View v) {
        Intent intent = new Intent(this, SearchActivity.class);
        startActivityForResult(intent, SEARCH_REQUEST);
    }

    public ArrayList<String> findPhotos() {
        File file = new File(getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).toString());
        ArrayList<String> photos = new ArrayList<>();
        File[] fList = file.listFiles();
        if (fList != null) {
            for (File f : fList) {
                photos.add(f.getPath());
            }
        }
        return photos;
    }

}