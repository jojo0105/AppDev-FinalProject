package com.example.discovery.ViewModels;

import com.example.discovery.Data.FavoriteDao;
import com.example.discovery.Data.FirebaseCallBackPark;
import com.example.discovery.Models.Park;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.Query;

public class FavoriteViewModel {
    public static FavoriteDao favoriteDao = new FavoriteDao();

    public static Task<Void> addToFavorite(Park park){
        return favoriteDao.addToFavorite(park);
    }

    public static Query removeFromFavorite(Park park){
        return favoriteDao.removeFromFavorite(park);
    }

    public static void readAllFav(FirebaseCallBackPark callBackPark){
        favoriteDao.readAllFav(callBackPark);
    }
}
