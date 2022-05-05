package com.example.discovery.Data;

import com.example.discovery.Util.DB;
import com.example.discovery.Util.Session;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

public class UserDao {
     private DatabaseReference usersModel;

    public UserDao() {
        this.usersModel = DB.selectCollection("users");;
    }

    public Query getUser(){
       return usersModel.orderByChild("userId").equalTo(Session.getInstance().getUserId());
    }
}
