package com.example.discovery.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.discovery.Data.FirebaseCallBackUser;
import com.example.discovery.Models.User;
import com.example.discovery.R;
import com.example.discovery.Util.DB;
import com.example.discovery.Util.Session;
import com.example.discovery.Util.Util;
import com.example.discovery.ViewModels.UserViewModel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnSuccessListener;
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
    private Button signOut;

    private FirebaseAuth firebaseAuth;
    private CollectionReference userModels = DB.selectCollection();
    private FirebaseUser user;

    private GoogleSignInOptions gso;
    private GoogleSignInClient googleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        this.overridePendingTransition(0, 0);
        initComponent();
        displayUser();

        bottomNavigationView.setSelectedItemId(R.id.setting_nav_btn);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Util.MenuClick(item, getApplicationContext());
                return false;
            }
        });

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
            }
        });
    }


    public void initComponent(){
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        firstname = findViewById(R.id.editTextFirstName);
        lastName = findViewById(R.id.editTextLastName);
        email = findViewById(R.id.editEmail);
        password = findViewById(R.id.editPassword);
        signOut = findViewById(R.id.signOut_btn);
        firstname.setEnabled(false);
        lastName.setEnabled(false);
        email.setEnabled(false);
        password.setEnabled(false);
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);
    }


    public void signOut() {
        if (user != null && firebaseAuth != null) {
            firebaseAuth.signOut();
            Session.getInstance().setUserId("");
            Session.getInstance().setUserName("");
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));

        }else if (googleSignInClient != null){
            googleSignInClient.signOut().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Session.getInstance().setUserId("");
                    Session.getInstance().setUserName("");
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                }
            });
        }

    }

    public void displayUser() {
        if (user != null && firebaseAuth != null) {
            UserViewModel.getUser(new FirebaseCallBackUser() {
                @Override
                public void onUseResponse(User user) {
                    firstname.setText(user.getFirstName());
                    lastName.setText(user.getLastname());
                    email.setText(user.getEmail());
                    char[] pwdChar = user.getPassword().toCharArray();
                    String displayPassword = "";
                    for (int i = 0; i < pwdChar.length; i++) {
                        displayPassword += "*";
                    }
                    password.setText(displayPassword);
                }

            });
        }
    }
}