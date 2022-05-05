package com.example.discovery.Activity;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.discovery.Adapter.OnParkClickListener;
import com.example.discovery.Adapter.ParkRecyclerViewAdapter;
import com.example.discovery.Models.Park;
import com.example.discovery.R;
import com.example.discovery.Util.Util;
import com.example.discovery.ViewModels.FavoriteViewModel;
import com.example.discovery.ViewModels.ParkViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class FavoritesActivity extends AppCompatActivity implements OnParkClickListener {
    private RecyclerView recyclerView;
    private ParkRecyclerViewAdapter parkRecyclerViewAdapter;
    private ParkViewModel parkViewModel;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        this.overridePendingTransition(0, 0);
        initComponent();
        loadFavPark();

        bottomNavigationView.setOnNavigationItemReselectedListener(item -> {
            Util.MenuClick(item, getApplicationContext());
        });
    }

    public void initComponent(){
        recyclerView = findViewById(R.id.fav_recycler);
        parkViewModel = ParkViewModel.getInstance(this);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
    }

    public void loadFavPark() {
        parkViewModel = ParkViewModel.getInstance(this);

        FavoriteViewModel.readAllFav(allFav -> {
            parkRecyclerViewAdapter = new ParkRecyclerViewAdapter(allFav, this, this, this);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(parkRecyclerViewAdapter);
        });


    }

    @Override
    public void onParkClicked(Park park) {
        Log.d("Park", "onclick:" + park.getFullName());
        parkViewModel.setSelectedPark(park);
        Log.d("Park", "onclick:" + parkViewModel.getSelectedPark().getValue());
        getSupportFragmentManager()
                .beginTransaction().replace(R.id.favorites_list, DetailsFragment.newInstance())
                .commit();
    }
}