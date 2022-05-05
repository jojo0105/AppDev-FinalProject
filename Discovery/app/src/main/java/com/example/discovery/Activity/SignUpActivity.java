package com.example.discovery.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.discovery.Models.User;
import com.example.discovery.R;
import com.example.discovery.Util.DB;
import com.example.discovery.Util.Session;
import com.example.discovery.Util.Util;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;

public class SignUpActivity extends AppCompatActivity {
   // private UserViewModel userViewModel;
    private EditText inputFname;
    private EditText inputLname;
    private EditText inputEmail;
    private EditText inputPassword;
    private EditText inputRE_Password;
    private Button register;

    private FirebaseAuth firebaseAuth;
    private  FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentUser;

    private CollectionReference usersModel = DB.selectCollection();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        firebaseAuth = FirebaseAuth.getInstance();
        authStateListener = (FirebaseAuth firebaseAuth) -> {currentUser = firebaseAuth.getCurrentUser();};


        inputFname = findViewById(R.id.inputFName_editText);
        inputLname = findViewById(R.id.inputLname_editText);
        inputEmail = findViewById(R.id.inputEmail_editText);
        inputPassword = findViewById(R.id.inputPassword_editText);
        inputRE_Password= findViewById(R.id.inputRe_Password_editText);
        register = findViewById(R.id.register_btn);
    }

    @Override
    protected void onStart() {
        super.onStart();
        currentUser = firebaseAuth.getCurrentUser();
        firebaseAuth.addAuthStateListener(authStateListener);

    }

    public void register(View view){
        Util.hideSoftKeyboard(view);
        User user = new User();

        String email = inputEmail.getText().toString().trim();
        String fName = inputFname.getText().toString().trim();
        String lName = inputLname.getText().toString().trim();
        String password = inputPassword.getText().toString().trim();
        String rePasword = inputRE_Password.getText().toString().trim();
        Session session = Session.getInstance();

        Intent intent = new Intent(getApplicationContext(), MapsActivity.class);

        if(!TextUtils.isEmpty(email)
                && !TextUtils.isEmpty(password)
                && !TextUtils.isEmpty(fName)
                && !TextUtils.isEmpty(lName)){
            if(!password.equalsIgnoreCase(rePasword)){
                Toast.makeText(this, "Password do not match", Toast.LENGTH_SHORT).show();
            } else if (password.length() < 6){
                Toast.makeText(this, "Password need to be more that 6 characters", Toast.LENGTH_SHORT).show();
            } else {
                firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            // get the created user
                            currentUser = firebaseAuth.getCurrentUser();

                            // if the currentUser is not null, curreentUserID = ID
                            assert currentUser != null;
                            String currentUserId = currentUser.getUid();




                            // set the value in the User Object
                            user.setUserId(currentUserId);
                            user.setFirstName(fName);
                            user.setLastname(lName);
                            user.setEmail(email);
                            user.setPassword(password);

                            // add to the DB, if success get the user and send to the map activity
                            usersModel.add(user)
                                    .addOnSuccessListener(documentReference -> documentReference
                                            .get()
                                            .addOnCompleteListener(task1 -> {
                                                if(task1.getResult().exists()){
                                                    session.setUserId(currentUserId);
                                                    session.setUserName(fName + " " + lName);
                                                    startActivity(intent);

                                                }
                                            }
                                    )).addOnFailureListener(e -> {});
                        }
                }}).addOnFailureListener(e -> {});
            }
        } else {
            Toast.makeText(this, "Please entre all the fields", Toast.LENGTH_SHORT).show();
        }
    }

}