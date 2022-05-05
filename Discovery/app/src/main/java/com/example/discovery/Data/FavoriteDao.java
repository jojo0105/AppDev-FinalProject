package com.example.discovery.Data;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.discovery.Models.Favorites;
import com.example.discovery.Models.Park;
import com.example.discovery.Util.DB;
import com.example.discovery.Util.Session;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FavoriteDao {
    private DatabaseReference favModels;

    public FavoriteDao() {
        this.favModels = DB.selectCollection("favorites");;
    }

    public Task<Void> addToFavorite(Park park) {
        Favorites favorite = new Favorites();
        favorite.setPark(park);
        favorite.setUserId(Session.getInstance().getUserId());

        return favModels.push().setValue(favorite);
    }


    public Query removeFromFavorite(Park park) {
        return favModels.orderByChild("userId").equalTo(Session.getInstance().getUserId());

//                whereEqualTo("userId", Session.getInstance().getUserId()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//            @Override
//            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                for(QueryDocumentSnapshot value : queryDocumentSnapshots){
//                    Park mpark = value.get("park", Park.class);
//                    if(park.getId().equalsIgnoreCase(mpark.getId())){
//                        favModels.document(value.getId()).delete();
//                    }
//                }
//            }
//        });

    }

    public  void readAllFav(FirebaseCallBackPark callBack){
        Log.d("Session_Fav", "User_id: " + Session.getInstance().getUserId());
         List<Park> favParkList = new ArrayList<>();
         favModels.orderByChild("userId").equalTo(Session.getInstance().getUserId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                favParkList.clear();
                for (DataSnapshot value : snapshot.getChildren()){
                    Favorites favorite = value.getValue(Favorites.class);
                    favParkList.add(favorite.getPark());
                }
                callBack.onParkResponse(favParkList);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
//        favModels.whereEqualTo("userId", Session.getInstance().getUserId()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//            @Override
//            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                for(QueryDocumentSnapshot value : queryDocumentSnapshots){
//                    favParkList.add(value.get("park", Park.class));
//                }
//
//            }
//        });

    }
}
