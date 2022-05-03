package com.example.discovery.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.discovery.R;
import com.example.discovery.Util.Util;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SettingActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        this.overridePendingTransition(0, 0);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemReselectedListener(item -> {
            Util.MenuClick(item, getApplicationContext());
        });
    }
}