package com.example.photogallery.View;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.photogallery.Contracts.MainContract;
import com.example.photogallery.Model.PhotoModel;
import com.example.photogallery.Presenter.GalleryPresenter;
import com.example.photogallery.Presenter.MainPresenter;
import com.example.photogallery.Presenter.PhotoAdapter;
import com.example.photogallery.R;
import com.example.photogallery.Utils;
import com.example.photogallery.databinding.ActivityMainBinding;
import com.example.photogallery.databinding.FragmentGalleryBinding;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends FragmentActivity implements MainContract.View {

    private ActivityMainBinding binding = null;

    private ArrayList<PhotoModel> photos;

    private MainPresenter mainPresenter;
    private PhotoAdapter photoAdapter;

    private GalleryPresenter galleryPresenter;

    public FragmentManager fragmentManager;
    private FragmentTransaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mainPresenter = new MainPresenter(this);

        initFragments();
    }

    public void initFragments() {
        fragmentManager = getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();
        Fragment homeFragment = new HomeFragment();
        transaction.replace(R.id.placeholderFragment, homeFragment);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Utils.REQUEST_IMAGE_CAPTURE) {
            if (resultCode == RESULT_OK) {
                for(Fragment f : getSupportFragmentManager().getFragments()) {
                    f.onActivityResult(requestCode, resultCode, data);
                }
            }
        }
    }
}