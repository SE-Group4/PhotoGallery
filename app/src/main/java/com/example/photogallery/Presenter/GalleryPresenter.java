package com.example.photogallery.Presenter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.photogallery.BuildConfig;
import com.example.photogallery.Contracts.GalleryContract;
import com.example.photogallery.Model.PhotoModel;
import com.example.photogallery.R;
import com.example.photogallery.Utils;
import com.example.photogallery.View.GalleryFragment;
import com.example.photogallery.View.HomeFragment;
import com.example.photogallery.View.MainActivity;
import com.example.photogallery.databinding.ActivityMainBinding;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class GalleryPresenter implements GalleryContract.Presenter {

    public Context context;
    private Fragment root;
    private PhotoAdapter photoAdapter;

    public GalleryPresenter(Context context, Fragment root, PhotoAdapter photoAdapter) {
        this.context = context;
        this.root = root;
        this.photoAdapter = photoAdapter;
    }

    @Override
    public void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photoFile = null;
        try { photoFile = createImageFile(); } catch (IOException ex) {}
        if (photoFile != null) {
            Uri photoURI = FileProvider.getUriForFile(context,BuildConfig.APPLICATION_ID + ".fileProvider", photoFile);
            Bitmap bitmap = (Bitmap) takePictureIntent.getParcelableExtra("BitmapImage");
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, bitmap);
            root.startActivityForResult(takePictureIntent, Utils.REQUEST_IMAGE_CAPTURE);
        }
    }

    String currentPhotoPath;

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = timeStamp + "_";
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName,".jpg", storageDir);

        currentPhotoPath = image.getAbsolutePath();
        return image;
    }


    // Load the saved photos here
    @Override
    public void loadPhotos() {
    }

    @Override
    public void updatePhotos() {
        photoAdapter.notifyDataSetChanged();
    }

    @Override
    public void addPhoto(PhotoModel photo) {
        photoAdapter.photos.add(photo);
        photoAdapter.notifyItemInserted(photoAdapter.photos.indexOf(photo));
    }

    @Override
    public void deletePhoto(PhotoModel photo) {
        photoAdapter.photos.remove(photo);
        photoAdapter.notifyItemRemoved(photoAdapter.photos.indexOf(photo));
    }

    @Override
    public void goBack() {
        FragmentTransaction transaction = root.getParentFragmentManager().beginTransaction();
        Fragment homeFragment = new HomeFragment();
        transaction.replace(R.id.placeholderFragment, homeFragment);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.commit();
    }
}
