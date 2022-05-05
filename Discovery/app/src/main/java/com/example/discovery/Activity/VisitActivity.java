package com.example.discovery.Activity;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.discovery.Adapter.TouchHelper;
import com.example.discovery.Adapter.VisitRecyclerViewAdapter;
import com.example.discovery.R;
import com.example.discovery.Util.Util;
import com.example.discovery.ViewModels.VisiteViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class VisitActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private VisitRecyclerViewAdapter  visitRecyclerViewAdapter;
    private BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit);
        this.overridePendingTransition(0, 0);
        initComponent();
        loadVisit();

        bottomNavigationView.setOnNavigationItemReselectedListener(item -> {
            Util.MenuClick(item, getApplicationContext());
        });

    }

    public void initComponent(){
        recyclerView = findViewById(R.id.fav_recycler);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
    }

    public void loadVisit() {
        VisiteViewModel.getAllVisit(visits -> {
            Log.d("VisitActivy", " lenght" + visits.size());
            visitRecyclerViewAdapter = new VisitRecyclerViewAdapter(visits, VisitActivity.this);
            ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new TouchHelper(visitRecyclerViewAdapter, visits));
            itemTouchHelper.attachToRecyclerView(recyclerView);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(visitRecyclerViewAdapter);
        });
    }


}