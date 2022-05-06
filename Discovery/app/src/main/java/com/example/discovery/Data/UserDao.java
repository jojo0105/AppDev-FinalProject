package com.example.discovery.Data;

import android.util.Log;

import com.example.discovery.Models.User;
import com.example.discovery.Util.DB;
import com.example.discovery.Util.Session;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
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

    public Task<DocumentReference> addUser(User user){
        return usersModel.add(user);
    }

    public void getUserByid(String id, FirebaseCallBackUser callBackUser) {
        Log.d("signIn", "userId" + id);
        usersModel.whereEqualTo("userId", id).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(queryDocumentSnapshots == null){
                    callBackUser.onUseResponse(null);
                }else{
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


            }
        });
    }

//    public void allUser(FirebaseCallBackUser callBackUser){
//        List<User> users = new ArrayList<>();
//        usersModel.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//            @Override
//            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                for(QueryDocumentSnapshot value : queryDocumentSnapshots){
//                    User user = new User();
//                    user.setFirstName(value.getString("firstName"));
//                    user.setLastname(value.getString("lastname"));
//                    user.setUserId(value.getString("userId"));
//                    user.setEmail(value.getString("email"));
//                    user.setPassword(value.getString("password"));
//                    users.add(user);
//                }
//                callBackUser.onUsesResponse(users);
//            }
//        });
//    }
}
