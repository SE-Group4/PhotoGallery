package com.example.photogallery;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;

public class PhotoPreviewActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_preview);

        Intent i = getIntent();
        String imagePath = i.getStringExtra("clickedImagePath");
        if (imagePath != null && !imagePath.isEmpty()) {
            File imgFile = new File(imagePath);

            // display image
            if (imgFile.exists()) {
                Bitmap b = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                ImageView img = (ImageView) findViewById(R.id.preview_image);
                img.setImageBitmap(b);
                TextView imgText = (TextView) findViewById((R.id.preview_image_text));
                imgText.setText(i.getStringExtra("clickedImageTimestamp"));
            }

        }
    }
}
