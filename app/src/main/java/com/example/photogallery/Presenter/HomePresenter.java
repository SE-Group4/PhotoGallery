package com.example.photogallery.Presenter;

import android.content.Context;
import android.util.Log;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.photogallery.Contracts.HomeContract;
import com.example.photogallery.R;
import com.example.photogallery.View.GalleryFragment;
import com.example.photogallery.View.HomeFragment;
import com.example.photogallery.View.MainActivity;

public class HomePresenter implements HomeContract.Presenter {

    private Context context;
    private FragmentManager fragmentManager;

    public HomePresenter(Context context, FragmentManager fragmentManager) {
        this.context = context;
        this.fragmentManager = fragmentManager;
    }

    @Override
    public void openGallery(View v) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        Fragment galleryFragment = new GalleryFragment();
        transaction.replace(R.id.placeholderFragment, galleryFragment);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.commit();
    }
}
