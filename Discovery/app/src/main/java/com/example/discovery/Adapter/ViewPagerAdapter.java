package com.example.discovery.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.discovery.Models.Images;
import com.example.discovery.Models.Park;
import com.example.discovery.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ViewPagerAdapter extends RecyclerView.Adapter<ViewPagerAdapter.ImageSlider> {
    private List<Images> imagesList;
    private Park park;
    private OnParkClickListener parkClickListener;

    public ViewPagerAdapter(Park park, OnParkClickListener parkClickListener) {
        this.imagesList = park.getImages();
        this.park = park;
        this.parkClickListener = parkClickListener;
    }

    public ViewPagerAdapter(Park park) {
        this.imagesList = park.getImages();
        this.park = park;
    }

    @NonNull
    @Override
    public ImageSlider onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_pager_row, parent, false);
        return new ImageSlider(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageSlider holder, int position) {
        Picasso.get()
                .load(imagesList.get(position).getUrl())
                .fit()
                .placeholder(android.R.drawable.stat_notify_error)
                .into(holder.imageView);


    }

    @Override
    public int getItemCount() {
        return imagesList.size();
    }

    public class ImageSlider extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final OnParkClickListener onParkClickListener;
        public ImageView imageView;

        public ImageSlider(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.view_pager_imageView);
            this.onParkClickListener = parkClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onParkClickListener.onParkClicked(park);
        }
    }
}
