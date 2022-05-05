package com.example.discovery.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.discovery.Data.FirebaseCallBackUser;
import com.example.discovery.Models.User;
import com.example.discovery.R;
import com.example.discovery.Util.DB;
import com.example.discovery.Util.Util;
import com.example.discovery.ViewModels.UserViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;

public class SettingActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private EditText firstname;
    private EditText lastName;
    private EditText email;
    private EditText password;

    private FirebaseAuth firebaseAuth;
    private CollectionReference userModels = DB.selectCollection();
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        this.overridePendingTransition(0, 0);
        initComponent();
        displayUser();


        bottomNavigationView.setOnNavigationItemReselectedListener(item -> {
            Util.MenuClick(item, getApplicationContext());
        });

    }


    public void initComponent(){
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        firstname = findViewById(R.id.editTextFirstName);
        lastName = findViewById(R.id.editTextLastName);
        email = findViewById(R.id.editEmail);
        password = findViewById(R.id.editPassword);
        firstname.setEnabled(false);
        lastName.setEnabled(false);
        email.setEnabled(false);
        password.setEnabled(false);
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
    }


    public void signOut(View view){
        if(user != null && firebaseAuth != null){
            firebaseAuth.signOut();
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        }
    }

    public void displayUser(){
        UserViewModel.getUser(new FirebaseCallBackUser() {
            @Override
            public void onUseResponse(User user) {
                firstname.setText(user.getFirstName());
                lastName.setText(user.getLastname());
                email.setText(user.getEmail());
                char[] pwdChar = user.getPassword().toCharArray();
                String displayPassword = "";
                for(int i = 0; i < pwdChar.length;i++){
                    displayPassword += "*";
                }
                password.setText(displayPassword);
            }
        });

    }
}