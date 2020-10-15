package com.example.photogallery;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.tweetcomposer.ComposerActivity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class PhotoPreviewActivity extends AppCompatActivity {
    private int index = 0;
    private ArrayList<String> photos = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_preview);

        Intent i = getIntent();
        String imagePath = i.getStringExtra("clickedImagePath");
        photos = i.getStringArrayListExtra("photoPaths");
        index = photos.indexOf(imagePath);
        if (photos.size() == 0) {
            displayPhoto(null);
        } else {
            displayPhoto(photos.get(index));
        }
        if (imagePath != null && !imagePath.isEmpty()) {
            File imgFile = new File(imagePath);

            // display image
            if (imgFile.exists()) {
                Bitmap b = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                ImageView img = (ImageView) findViewById(R.id.preview_image);
                img.setImageBitmap(b);
                TextView imgText = (TextView) findViewById((R.id.preview_image_text));
                imgText.setText(i.getStringExtra("clickedImageTimestamp"));

                // display share button
                if (TwitterCore.getInstance().getSessionManager().getActiveSession() != null) {
                    Button accountBtn = (Button) findViewById(R.id.btnShare);
                    if (accountBtn != null) {
                        accountBtn.setVisibility(View.VISIBLE);
                        accountBtn.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                shareOnTwitter(imgFile);
                            }
                        });
                    }
                }
            }

        }
    }

    public void scrollPhotos(View v) {
        updatePhoto(photos.get(index), ((EditText) findViewById(R.id.etCaption)).getText().toString());
        switch (v.getId()) {
            case R.id.btnPrev:
                if (index > 0) {
                    index--;
                }
                break;
            case R.id.btnNext:
                if (index < (photos.size() - 1)) {
                    index++;
                }
                break;
            default:
                break;
        }
        displayPhoto(photos.get(index));
    }

    public void shareOnTwitter(File selectedPhoto) {
        String captionText = ((EditText) findViewById(R.id.etCaption)).getText().toString();
        Uri photoUri = Uri.fromFile(selectedPhoto);

        // set default twit text if caption is empty
        if(captionText == null || captionText.isEmpty()) {
            captionText = "COMP 7082";
        }

        final TwitterSession session = TwitterCore.getInstance().getSessionManager().getActiveSession();
        // invoke Twitter sharing
        final Intent intent = new ComposerActivity.Builder(PhotoPreviewActivity.this)
                .session(session)
                .image(photoUri)
                .text(captionText)
                .hashtags("#photogallery")
                .createIntent();
        startActivity(intent);
    }

    private void displayPhoto(String path) {
        ImageView iv = findViewById(R.id.preview_image);
        TextView tv = findViewById(R.id.preview_image_text);
        EditText et = findViewById(R.id.etCaption);
        try {
            final ExifInterface ei = new ExifInterface(path);
            String timestamp = ei.getAttribute(ExifInterface.TAG_DATETIME);

            if (path == null || path.equals("")) {
                iv.setImageResource(R.mipmap.ic_launcher);
                et.setText("");
                tv.setText("");
            } else {
                iv.setImageBitmap(BitmapFactory.decodeFile(path));
                String[] attr = path.split("_");
                et.setText(attr[1]);
                tv.setText("Timestamp: " + timestamp);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        Intent intentResult = new Intent();
        intentResult.putExtra(MainActivity.PHOTO_PATHS, photos);
        setResult(RESULT_OK, intentResult);
        finish();
    }

    private void updatePhoto(String path, String caption) {
        String[] attr = path.split("_");
        String newPath = attr[0] + "_" + caption + "_" + attr[2] + "_" + attr[3];

        if (attr.length >= 3) {
            File to = new File(newPath);
            File from = new File(path);
            from.renameTo(to);
            photos.set(index, newPath);
        }
    }
}
