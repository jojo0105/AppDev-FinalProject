package com.example.discovery.ViewModels;

import com.example.discovery.Data.FirebaseCallBackUser;
import com.example.discovery.Data.UserDao;

public class UserViewModel {
    public static UserDao userDao = new UserDao();

    public static void getUser(FirebaseCallBackUser callBackUser){
       userDao.getUser(callBackUser);
    }

//    public static  Task<Void> addUser(User user){
//        return userDao.addUser(user);
//    }
}
