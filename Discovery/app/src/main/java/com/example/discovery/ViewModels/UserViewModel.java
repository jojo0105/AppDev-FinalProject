package com.example.discovery.ViewModels;

import com.example.discovery.Data.UserDao;
import com.google.firebase.database.Query;

public class UserViewModel {
    public static UserDao userDao = new UserDao();

    public static Query getUser(){
        return userDao.getUser();
    }
}
