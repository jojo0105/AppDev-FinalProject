package com.example.discovery.Activity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.discovery.Adapter.FavoriteRecyclerViewAdapter;
import com.example.discovery.Adapter.OnParkClickListener;
import com.example.discovery.Data.FavoriteDao;
import com.example.discovery.Models.Park;
import com.example.discovery.Models.ParkViewModel;
import com.example.discovery.R;

import java.util.ArrayList;
import java.util.List;


public class FavoritesFragment extends Fragment implements OnParkClickListener {
    private List<Park> parkList;
    private List<Park> favParkList;
    private RecyclerView recyclerView;
    private FavoriteRecyclerViewAdapter favoriteRecyclerViewAdapter;
    private ParkViewModel parkViewModel;

    public FavoritesFragment() {
        // Required empty public constructor
    }


    public static FavoritesFragment newInstance() {
        FavoritesFragment fragment = new FavoritesFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parkList = new ArrayList<>();
        favParkList = new ArrayList<>();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        parkViewModel = new ViewModelProvider(requireActivity())
                .get(ParkViewModel.class);


        FavoriteDao.readAllFav(allFav -> {
            for(String favParkID : allFav){
                if (parkViewModel.getParks().getValue() != null){
                    parkList = parkViewModel.getParks().getValue();
                    for(Park park: parkList){
                        if(favParkID.equals(park.getId()))
                            favParkList.add(park);
                    }
                }
            }

            Log.d("FavoriteFragment", "AllFavPark: " + favParkList.size());
            favoriteRecyclerViewAdapter = new FavoriteRecyclerViewAdapter(favParkList);
            recyclerView.setAdapter(favoriteRecyclerViewAdapter);
        });

        Log.d("FavoriteFragment", "AllFavPark: " + favParkList.size());


    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);
        recyclerView = view.findViewById(R.id.fav_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
      return view;
    }

    @Override
    public void onParkClicked(Park park) {
        Log.d("Park", "onclick:" + park.getFullName());
        parkViewModel.setSelectedPark(park);
        getActivity().getSupportFragmentManager()
                .beginTransaction().replace(R.id.park_fragment, DetailsFragment.newInstance())
                .commit();

    }
}