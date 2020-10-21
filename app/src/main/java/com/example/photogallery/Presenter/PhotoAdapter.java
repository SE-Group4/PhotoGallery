package com.example.photogallery.Presenter;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.photogallery.Model.PhotoModel;
import com.example.photogallery.R;
import com.example.photogallery.databinding.ActivityMainBinding;
import com.example.photogallery.databinding.ItemPhotoBinding;

import java.io.File;
import java.util.ArrayList;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.ViewHolder> {

    public ArrayList<PhotoModel> photos;

    public PhotoAdapter() {
        photos = new ArrayList<PhotoModel>();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView photo;
        public TextView caption;
        public ItemPhotoBinding binding;

        public ViewHolder(ItemPhotoBinding item) {
            super(item.getRoot());

            photo = item.photo;
            caption = item.photoCaption;
            binding = item;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemPhotoBinding item = ItemPhotoBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(photos.size() == 0) return;
        holder.caption.setText(photos.get(position).getCaption());
        holder.photo.setImageBitmap(photos.get(position).getBitmap());

    }

    @Override
    public int getItemCount() {
        return photos.size();
    }
}
