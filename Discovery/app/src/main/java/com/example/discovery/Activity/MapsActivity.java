package com.example.discovery.Activity;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.discovery.Adapter.CustomInfoWindow;
import com.example.discovery.Data.FavoriteDao;
import com.example.discovery.Data.ParkRepository;
import com.example.discovery.Models.Favorites;
import com.example.discovery.Models.Park;
import com.example.discovery.Models.ParkViewModel;
import com.example.discovery.R;
import com.example.discovery.Util.AsyncResponse;
import com.example.discovery.Util.DB;
import com.example.discovery.Util.Session;
import com.example.discovery.Util.Util;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.discovery.databinding.ActivityMapsActitviyBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener{



    private GoogleMap mMap;
    public static ParkViewModel parkViewModel;
    private List<Park> parkList;
    private CardView cardView;
    private EditText stateCodeInput;
    private ImageButton searchBtn;
    private String code = "";


    private String currentUserId;





    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser user;
    private CollectionReference userModels = DB.selectCollection("users");





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_actitviy);
        this.overridePendingTransition(0, 0);

        parkViewModel = ParkViewModel.getInstance(this);


        firebaseAuth = FirebaseAuth.getInstance();
        currentUserId = Session.getInstance().getUserId();
        authStateListener = (FirebaseAuth firebaseAuth) -> {user = firebaseAuth.getCurrentUser();};



        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        cardView = findViewById(R.id.cardView);
        stateCodeInput = findViewById(R.id.floating_state_value_et);
        searchBtn = findViewById(R.id.floating_search_button);

        searchBtn.setOnClickListener(view -> {
            parkList.clear();
            Util.hideSoftKeyboard(view);
            String stateCode = stateCodeInput.getText().toString().trim();
            if(!TextUtils.isEmpty(stateCode)){
                code = stateCode;
                parkViewModel.selectCode(code);
                onMapReady(mMap);
                stateCodeInput.setText("");
            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Util.MenuClick(item, getApplicationContext());

//            if(id == R.id.maps_nav_btn){
//                if (cardView.getVisibility() == View.INVISIBLE ||
//                        cardView.getVisibility() == View.GONE) {
//                    cardView.setVisibility(View.VISIBLE);
//                }
//                parkList.clear();
//                mMap.clear();
//                getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.map, mapFragment)
//                        .commit();
//               mapFragment.getMapAsync(this);
//                 return true;
//            }else if (id == R.id.parks_nav_btn){
//                cardView.setVisibility(View.GONE);
//                Intent intent = new Intent(getApplicationContext(), ParksListActivity.class);
//                startActivity(intent);
//
//               // selectedFragment = ParksFragment.newInstance();
//            }else if (id == R.id.fav_nav_btn){
//                selectedFragment = FavoritesFragment.newInstance();
//                cardView.setVisibility(View.GONE);
//            }else if (id == R.id.setting_nav_btn){
//                selectedFragment = SettingFragment.newInstance();
//                cardView.setVisibility(View.GONE);
//            }
//            getSupportFragmentManager().beginTransaction()
//                    .replace(R.id.map, selectedFragment)
//                    .commit();
            return true;
        });
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setInfoWindowAdapter(new CustomInfoWindow(getApplicationContext()));
        mMap.setOnInfoWindowClickListener(this);

        parkList = new ArrayList<>();
        parkList.clear();

        populateMap();
    }

    private void populateMap() {
        mMap.clear();
        ParkRepository.getParks((AsyncResponse) parks -> {
            parkList = parks;
            for (Park park : parks) {
                LatLng location = new LatLng(Double.parseDouble(park.getLatitude()),
                        Double.parseDouble(park.getLongitude()));

                MarkerOptions markerOptions = new MarkerOptions()
                        .position(location)
                        .title(park.getName())
                        .snippet(park.getStates());

                Marker marker = mMap.addMarker(markerOptions);
                marker.setTag(park);

                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 5));
                Log.d("onMapReady:", park.getFullName());
            }
            parkViewModel.setSelectedParks(parkList);
            Log.d("onMapReady:",parkViewModel.getParks().getValue() + "");

        },code);
    }


//    public void onFavoriteParkClick(View view) {
//        ToggleButton toggleButton = view.findViewById(R.id.fav_btn);
//
//        Log.d("Fav_Click", "here");
//        Park park = parkViewModel.getSelectedPark().getValue();
       // Log.d("Fav_Click", park.getId());
//            if(toggleButton.isChecked()){
//                Log.d("Fav_Click", toggleButton.isChecked() + "");
//                Favorites favorite = new Favorites();
//                favorite.setParkId(park.getId());
//                favorite.setUserId(currentUserId);
//
//                Log.d("Fav_Click", favorite.getParkId() + " " + favorite.getUserId());
//                favModels.add(favorite).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                    @Override
//                    public void onSuccess(DocumentReference documentReference) {
//                        Log.d("Fav_Click", "added");
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.d("Fav_Click", e.toString());
//                    }
//                });
//            } else {
//                //
//            }


        //TO-DO: insert the favorite into the Favorite table fav_Table [fav_id, user_id, park_id]
//        Toast.makeText(this,"Added to Favorites",Toast.LENGTH_SHORT).show();
//    }
    public void onAddedScheduleClick(View view) {
        Toast.makeText(this,"Added to Schedule",Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onInfoWindowClick(@NonNull Marker marker) {
        cardView.setVisibility(View.GONE);
        goToDetailsFragment(marker);
    }

    private void goToDetailsFragment(@NonNull Marker marker) {
        parkViewModel.setSelectedPark((Park) marker.getTag());
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.map, DetailsFragment.newInstance())
                .commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
        user = firebaseAuth.getCurrentUser();
        firebaseAuth.addAuthStateListener(authStateListener);

    }
}