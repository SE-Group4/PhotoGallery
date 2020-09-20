package com.example.photogallery;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CameraActivity extends AppCompatActivity {
    private File newPhoto;
    static final int REQUEST_TAKE_PHOTO = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
    }

    // deletes image if user canceled the action
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED){
            newPhoto.delete();
        }
    }

    // referenced from activity_camera.xml
    public void takePhoto(View v) throws IOException {
        dispatchTakePictureIntent();
    }

    // invoke camera
    private void dispatchTakePictureIntent() throws IOException {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        newPhoto = createImageFile();

        if (newPhoto != null) {

            // get a URI for the file based on authority of app's own fileprovider
            Uri photoURI = FileProvider.getUriForFile(this, "com.example.photogallery.fileprovider", newPhoto);

            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI); // FIXME: uncomment when saving images. NOTE: sets Bitmap data to null
            startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
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
        String imageFileName = "JPEG_" + timeStamp + "_";

        // prepares the final path for the output photo file
        File image = File.createTempFile(imageFileName,".jpg", downloadFolder);

        return image;
    }
}