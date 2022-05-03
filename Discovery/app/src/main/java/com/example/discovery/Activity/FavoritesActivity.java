package com.example.discovery.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.discovery.Adapter.FavoriteRecyclerViewAdapter;
import com.example.discovery.Adapter.OnParkClickListener;
import com.example.discovery.Data.FavoriteDao;
import com.example.discovery.Models.Park;
import com.example.discovery.Models.ParkViewModel;
import com.example.discovery.R;
import com.example.discovery.Util.Util;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class FavoritesActivity extends AppCompatActivity implements OnParkClickListener {
    private RecyclerView recyclerView;
    private FavoriteRecyclerViewAdapter favoriteRecyclerViewAdapter;
    private List<Park> parkList;
    private List<Park> favParkList;
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
        parkList = new ArrayList<>();
        favParkList = new ArrayList<>();
    }


    public void loadFavPark(){
        parkViewModel = ParkViewModel.getInstance(this);

        FavoriteDao.readAllFav(allFav -> {

            favoriteRecyclerViewAdapter = new FavoriteRecyclerViewAdapter(allFav, this);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(favoriteRecyclerViewAdapter);

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