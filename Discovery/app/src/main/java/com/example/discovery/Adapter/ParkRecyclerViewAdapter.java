package com.example.discovery.Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.discovery.Activity.DetailsFragment;
import com.example.discovery.Models.Favorites;
import com.example.discovery.Models.Park;
import com.example.discovery.R;
import com.example.discovery.ViewModels.FavoriteViewModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class ParkRecyclerViewAdapter extends RecyclerView.Adapter<ParkRecyclerViewAdapter.ViewHolder> {
    private final List<Park> parkList;
    private final OnParkClickListener parkClickListener;
    public ViewPagerAdapter viewPagerAdapter;


    public ParkRecyclerViewAdapter(List<Park> parkList, OnParkClickListener onParkClickListener)  {
        this.parkList = parkList;
        this.parkClickListener = onParkClickListener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.park_row, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Park park = parkList.get(position);

        holder.parkName.setText(park.getName());
        holder.parkType.setText(park.getDesignation());
//        holder.parkState.setText(park.getStates());

        FavoriteViewModel.readAllFav(allFav -> {
            for(Park favPark : allFav){
                if(favPark.getId().equals(park.getId())){
                    holder.fav.setChecked(true);
                }
            }
        });

        holder.fav.setOnClickListener(view -> {
            if(holder.fav.isChecked()){
                FavoriteViewModel.addToFavorite(park);
            } else {
                FavoriteViewModel.removeFromFavorite(park).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot value : snapshot.getChildren()){
                            Favorites favorite = value.getValue(Favorites.class);
                            if(park.getId().equalsIgnoreCase(favorite.getPark().getId())){
                                value.getRef().removeValue();
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("DetailsFragments", "onCancelled", error.toException());
                    }
                });
            }
            
        });

        holder.schedule.setOnClickListener(view -> {
            if(holder.schedule.isChecked()){
                DetailsFragment.onAddedScheduleClick(holder.itemView, holder.itemView.getContext(),park);
            }
        });

        viewPagerAdapter = new ViewPagerAdapter(park.getImages());
        holder.viewPager.setAdapter(viewPagerAdapter);

//        if (park.getImages().size() > 0) {
//            Picasso.get()
//                    .load(park.getImages().get(0).getUrl())
//                    .placeholder(android.R.drawable.stat_sys_download)
//                    .error(android.R.drawable.stat_notify_error)
//                    .fit()
//                    .centerCrop()
//                    .into(holder.parkImage);
//        }
    }



    @Override
    public int getItemCount() {
        return parkList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public ImageView parkImage;
        public TextView parkName;
        public TextView parkType;
        ViewPager2 viewPager;
        public ToggleButton fav;
        public ToggleButton schedule;
        OnParkClickListener onParkClickListener;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            parkImage = itemView.findViewById(R.id.row_park_imageView);
            parkName = itemView.findViewById(R.id.visite_parkName_textView);
            parkType = itemView.findViewById(R.id.schedule_date_textView);
            viewPager = itemView.findViewById(R.id.detail_viewPager);
            fav = itemView.findViewById(R.id.fav_btn);
            schedule = itemView.findViewById(R.id.schedule_btn);

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
