package com.example.discovery.Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.discovery.Activity.DetailsFragment;
import com.example.discovery.Models.Favorites;
import com.example.discovery.Models.Park;
import com.example.discovery.R;
import com.example.discovery.ViewModels.FavoriteViewModel;
import com.example.discovery.ViewModels.ParkViewModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class ParkRecyclerViewAdapter extends RecyclerView.Adapter<ParkRecyclerViewAdapter.ViewHolder> implements OnParkClickListener{
    private final List<Park> parkList;
    private final OnParkClickListener parkClickListener;
    public ViewPagerAdapter viewPagerAdapter;
    private AppCompatActivity activity;
    private ParkViewModel parkViewModel;



    public ParkRecyclerViewAdapter(List<Park> parkList, OnParkClickListener parkClickListener, ViewModelStoreOwner activity, AppCompatActivity activity1)  {
        this.parkList = parkList;
        this.parkClickListener = parkClickListener;
        this.activity = activity1;
        parkViewModel = ParkViewModel.getInstance(activity);
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

        viewPagerAdapter = new ViewPagerAdapter(park,this);
        holder.viewPager.setAdapter(viewPagerAdapter);

    }



    @Override
    public int getItemCount() {
        return parkList.size();
    }

    @Override
    public void onParkClicked(Park park) {
        Log.d("Park", "onclick:" + park.getFullName());

        parkViewModel.setSelectedPark(park);
        Log.d("Park", "onclick:" + parkViewModel.getSelectedPark().getValue());
        parkViewModel.setSelectedPark(park);
        activity.getSupportFragmentManager()
                .beginTransaction().replace(R.id.park_list, DetailsFragment.newInstance())
                .commit();

    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final OnParkClickListener onParkClickListener;
        public ImageView parkImage;
        public TextView parkName;
        public TextView parkType;
        ViewPager2 viewPager;
        public ToggleButton fav;
        public ToggleButton schedule;
        public View view;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            parkImage = itemView.findViewById(R.id.row_park_imageView);
            parkName = itemView.findViewById(R.id.visite_parkName_textView);
            parkType = itemView.findViewById(R.id.schedule_date_textView);
            viewPager = itemView.findViewById(R.id.detail_viewPager);
            fav = itemView.findViewById(R.id.fav_btn);
            schedule = itemView.findViewById(R.id.schedule_btn);
            view = itemView.findViewById(R.id.row_layout);

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
