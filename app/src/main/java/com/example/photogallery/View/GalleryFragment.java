package com.example.photogallery.View;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.photogallery.Contracts.GalleryContract;
import com.example.photogallery.Model.PhotoModel;
import com.example.photogallery.Presenter.GalleryPresenter;
import com.example.photogallery.Presenter.PhotoAdapter;
import com.example.photogallery.R;
import com.example.photogallery.Utils;
import com.example.photogallery.databinding.FragmentGalleryBinding;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class GalleryFragment extends Fragment implements GalleryContract.View {

    private PhotoAdapter photoAdapter;
    private GalleryPresenter galleryPresenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        FragmentGalleryBinding binding = FragmentGalleryBinding.inflate(inflater);

        binding.snapBtn.setOnClickListener(this::onTakeSnap);
        binding.backBtn.setOnClickListener(this::onBack);

        photoAdapter = new PhotoAdapter();
        binding.photoRecyclerView.setAdapter(photoAdapter);
        binding.photoRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 4)); // Span = collums

        galleryPresenter = new GalleryPresenter(getContext(), this, photoAdapter);

        return binding.getRoot();
    }

    @Override
    public void onTakeSnap(View view) {
        galleryPresenter.openCamera();
        galleryPresenter.updatePhotos();
    }

    @Override
    public void onBack(View view) {
        galleryPresenter.goBack();
    }

    @Override
    public void onActivityResult (int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Utils.REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            Log.d("Result", "onActivityResult: " + imageBitmap);
            galleryPresenter.addPhoto(new PhotoModel("default", imageBitmap));
        }

//        if (requestCode == SEARCH_ACTIVITY_REQUEST_CODE) {
//            if (resultCode == SearchActivity.FILTER_APPLIED) {
//                // presenter.handleRequestSearchFilter(data, imageAdapter);
//            } else if (resultCode == SearchActivity.FILTER_CLEARED) {
//                // presenter.handleSearchFiltersCleared(imageAdapter);
//            }
        }
}
