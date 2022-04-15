package com.example.discovery.Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.discovery.Data.FavoriteDao;
import com.example.discovery.Data.FirebaseCallBack;
import com.example.discovery.Models.Favorites;
import com.example.discovery.Models.Images;
import com.example.discovery.Models.Park;
import com.example.discovery.Models.ParkViewModel;
import com.example.discovery.R;
import com.example.discovery.Util.DB;
import com.example.discovery.Util.Session;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ParkRecyclerViewAdapter extends RecyclerView.Adapter<ParkRecyclerViewAdapter.ViewHolder> {
    private final List<Park> parkList;
    private final OnParkClickListener parkClickListener;
    private FavoriteDao favoriteDao;
    private  static CollectionReference favModels = DB.selectCollection("favorites");




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
        holder.parkState.setText(park.getStates());

        FavoriteDao.readAllFav(allFav -> {
            for(String favParkID : allFav){
                if(favParkID.equals(park.getId())){
                    holder.fav.setChecked(true);
                }

            }
        });



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
