package com.example.discovery.ViewModels;

import com.example.discovery.Data.FirebaseCallBackUser;
import com.example.discovery.Data.UserDao;
import com.example.discovery.Models.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;

public class UserViewModel {
    public static UserDao userDao = new UserDao();

    public static void getUser(FirebaseCallBackUser callBackUser){
       userDao.getUser(callBackUser);
    }

    public static Task<DocumentReference> addUser(User user){
        return userDao.addUser(user);
    }

    public static void getUserById( String id, FirebaseCallBackUser callBackUser){
        userDao.getUserByid(id, callBackUser);
    }

}
