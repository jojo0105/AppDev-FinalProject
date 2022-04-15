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
        favorite.setParkId(park.getId());
        favorite.setUserId(Session.getInstance().getUserId());

        favModels.add(favorite).addOnSuccessListener(documentReference -> Log.d("Fav_Click", "added"))
                .addOnFailureListener(e -> Log.d("Fav_Click", e.toString()));

    }


    public static void removeFromFavorite(Park park) {
       favModels.whereEqualTo("parkId", park.getId()).addSnapshotListener(new EventListener<QuerySnapshot>() {
           @Override
           public void onEvent(@Nullable QuerySnapshot values, @Nullable FirebaseFirestoreException error) {
               if(error != null){
               }

               assert values != null;
               for(QueryDocumentSnapshot value : values){
                   if(value.getString(Util.KEY_USERID).equals(Session.getInstance().getUserId()))
                       favModels.document(value.getId()).delete();
               }
           }
       });
    }

    public static void readAllFav(FirebaseCallBack callBack){
        ArrayList<String> favParkList = new ArrayList<>();



        favModels.whereEqualTo("userId", Session.getInstance().getUserId()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(QueryDocumentSnapshot value : queryDocumentSnapshots){
                    String data = value.getString("parkId");
                    favParkList.add(data);
                }
                callBack.onResponse(favParkList);
            }
        });

    }




  //  public static List<String> getAllFav (String userId){



//        favModels.whereEqualTo("userId", Session.getInstance().getUserId()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if (task.isSuccessful()) {
//                    for(QueryDocumentSnapshot value : task.getResult()){
//                        String data = value.getString("parkId");
//                        favParkList.add(data);
//                    }
//                }
//
//
//            }
//        });



//        favModels.whereEqualTo("userId", userId).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//            @Override
//            public void onSuccess(QuerySnapshot values) {
//                for(QueryDocumentSnapshot value : values) {
//                    String data = value.getString("parkId");
//                    favParkList.add(data);
//                }
//                Log.d("Fav_Click_Fav", favParkList.size() + "");
//            }
//        });


//        })new EventListener<QuerySnapshot>() {
//            @Override
//            public void onEvent(@Nullable QuerySnapshot values, @Nullable FirebaseFirestoreException error) {
//                if(error != null){
//                }
//                assert values != null;
//                for(QueryDocumentSnapshot value : values){
//
//                    Log.d("Fav_Click_Dao", value.getString("parkId"));
//                    String data = value.getString("parkId");
//                    favParkList.add(data);
//                }
//
//                Log.d("Fav_Click_arraysList", favParkList.size() + "");
//            }
        //});

    //    Log.d("Fav_Click_arraysList", favParkList.size() + "");
   //     return favParkList;
   // }
}
