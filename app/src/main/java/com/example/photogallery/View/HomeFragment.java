package com.example.photogallery.View;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.photogallery.Contracts.GalleryContract;
import com.example.photogallery.Contracts.HomeContract;
import com.example.photogallery.Presenter.HomePresenter;
import com.example.photogallery.R;
import com.example.photogallery.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment implements HomeContract.View {

    private HomePresenter homePresenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        FragmentHomeBinding binding = FragmentHomeBinding.inflate(inflater);
        binding.localBtn.setOnClickListener(this::displayGallery);

        homePresenter = new HomePresenter(this.getContext(), this.getParentFragmentManager());

        return binding.getRoot();
    }

    private void displayGallery(View view) {
        homePresenter.openGallery(view);
    }

}

