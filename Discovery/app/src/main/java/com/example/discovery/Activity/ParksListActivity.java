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
import com.example.discovery.ViewModels.ParkViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class ParksListActivity extends AppCompatActivity implements OnParkClickListener{
    private RecyclerView recyclerView;
    private ParkRecyclerViewAdapter parkRecyclerViewAdapter;
    private List<Park> parkList;
    private ParkViewModel parkViewModel;
    private BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parks_list);
        this.overridePendingTransition(0, 0);
        initComponent();
        loadParkList();

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Util.MenuClick(item, getApplicationContext());
            return true;
        });

        Log.d("ParkFragment", "parkViewModels: " + parkViewModel.getParks().getValue());
    }

    public void initComponent(){
        recyclerView = findViewById(R.id.park_recycler);
        parkViewModel = ParkViewModel.getInstance(this);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
    }


     public void loadParkList(){
         if (parkViewModel.getParks().getValue() != null) {
             parkList = parkViewModel.getParks().getValue();
             parkRecyclerViewAdapter = new ParkRecyclerViewAdapter(parkList, this);

             recyclerView.setAdapter(parkRecyclerViewAdapter);
             recyclerView.setHasFixedSize(true);
             recyclerView.setLayoutManager(new LinearLayoutManager(this));
         }


     }

    @Override
    public void onParkClicked(Park park) {
        Log.d("Park", "onclick:" + park.getFullName());
        parkViewModel.setSelectedPark(park);
        Log.d("Park", "onclick:" + parkViewModel.getSelectedPark().getValue());
        getSupportFragmentManager()
                .beginTransaction().replace(R.id.park_list, DetailsFragment.newInstance())
                .commit();
    }
}