package com.example.discovery.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.discovery.Data.FavoriteDao;
import com.example.discovery.Models.Park;
import com.example.discovery.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FavoriteRecyclerViewAdapter extends RecyclerView.Adapter<FavoriteRecyclerViewAdapter.ViewHolder> {
    private final List<Park> parkList;
    private final OnParkClickListener parkClickListener;



    public FavoriteRecyclerViewAdapter(List<Park> parkList, OnParkClickListener parkClickListener) {
        this.parkList = parkList;
        this.parkClickListener = parkClickListener;
    }

    @NonNull
    @Override
    public FavoriteRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.park_row, parent, false);

        return new FavoriteRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteRecyclerViewAdapter.ViewHolder holder, int position) {
        Park park = parkList.get(position);

        holder.parkName.setText(park.getName());
        holder.parkType.setText(park.getDesignation());
        holder.parkState.setText(park.getStates());
        holder.fav.setChecked(true);

        holder.fav.setOnClickListener(view -> {
            if(holder.fav.isChecked()){
                FavoriteDao.addToFavorite(park);
            } else {
                FavoriteDao.removeFromFavorite(park);
            }

        });
        if (park.getImages().size() > 0) {
            Picasso.get()
                    .load(park.getImages().get(0).getUrl())
                    .placeholder(android.R.drawable.stat_sys_download)
                    .error(android.R.drawable.stat_notify_error)
                    .resize(100, 100)
                    .centerCrop()
                    .into(holder.parkImage);
        }
    }



    @Override
    public int getItemCount() {
        return parkList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public ImageView parkImage;
        public TextView parkName;
        public TextView parkType;
        public TextView parkState;
        public ToggleButton fav;
        OnParkClickListener onParkClickListener;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            parkImage = itemView.findViewById(R.id.row_park_imageView);
            parkName = itemView.findViewById(R.id.row_parkName_textView);
            parkType = itemView.findViewById(R.id.row_park_type_textView);
            parkState = itemView.findViewById(R.id.row_park_state_textView);
            fav = itemView.findViewById(R.id.fav_btn);

            this.onParkClickListener = parkClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Park currPark = parkList.get(getAdapterPosition());
            onParkClickListener.onParkClicked(currPark);
        }
    }

}
