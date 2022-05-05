package com.example.discovery.Data;

import com.example.discovery.Models.User;
import com.example.discovery.Util.DB;
import com.example.discovery.Util.Session;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class UserDao {

     //private DatabaseReference usersModel;
     private CollectionReference usersModel;

    public UserDao() {
        this.usersModel = DB.selectCollection();
        //this.usersModel = DB.selectCollection("users");
    }

    public void getUser(FirebaseCallBackUser callBackUser) {
         usersModel.whereEqualTo("userId", Session.getInstance().getUserId()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot value : queryDocumentSnapshots) {
                   User user = new User();
                    user.setFirstName(value.getString("firstName"));
                    user.setLastname(value.getString("lastname"));
                    user.setUserId(value.getString("userId"));
                    user.setEmail(value.getString("email"));
                    user.setPassword(value.getString("password"));
                   callBackUser.onUseResponse(user);
                }

            }
        });
    }

//    public Task<Void> addUser(User user){
//        return usersModel.push().setValue(user);
//    }
}
