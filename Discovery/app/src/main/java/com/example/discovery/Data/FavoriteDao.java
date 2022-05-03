package com.example.discovery.Data;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Database;

import com.example.discovery.Models.Favorites;
import com.example.discovery.Models.Park;
import com.example.discovery.Util.DB;
import com.example.discovery.Util.Session;
import com.example.discovery.Util.Util;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class FavoriteDao {
    private  static CollectionReference favModels = DB.selectCollection("favorites");



    public static void addToFavorite(Park park) {
        Favorites favorite = new Favorites();
        favorite.setPark(park);
        favorite.setUserId(Session.getInstance().getUserId());

        favModels.add(favorite).addOnSuccessListener(documentReference -> Log.d("Fav_Click", "added"))
                .addOnFailureListener(e -> Log.d("Fav_Click", e.toString()));

    }


    public static void removeFromFavorite(Park park) {
        favModels.whereEqualTo("userId", Session.getInstance().getUserId()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(QueryDocumentSnapshot value : queryDocumentSnapshots){
                    Park mpark = value.get("park", Park.class);
                    if(park.getId().equalsIgnoreCase(mpark.getId())){
                        favModels.document(value.getId()).delete();
                    }
                }
            }
        });



//       favModels.document().collection("park").whereEqualTo("id", park.getId()).addSnapshotListener(new EventListener<QuerySnapshot>() {
//           @Override
//           public void onEvent(@Nullable QuerySnapshot values, @Nullable FirebaseFirestoreException error) {
//               assert values != null;
//               for(QueryDocumentSnapshot value : values){
//                   if(value.getString(Util.KEY_USERID).equals(Session.getInstance().getUserId()))
//                       favModels.document(value.getId()).delete();
//               }
//           }
//       });

    }

    public static void readAllFav(FirebaseCallBack callBack){
        ArrayList<Park> favParkList = new ArrayList<>();

        favModels.whereEqualTo("userId", Session.getInstance().getUserId()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(QueryDocumentSnapshot value : queryDocumentSnapshots){
                    favParkList.add(value.get("park", Park.class));
                }
                callBack.onResponse(favParkList);
            }
        });

    }
}
