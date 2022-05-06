package com.example.discovery.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.discovery.GithubAuthActivity;
import com.example.discovery.Models.User;
import com.example.discovery.R;
import com.example.discovery.Util.DB;
import com.example.discovery.Util.Session;
import com.example.discovery.Util.Util;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class LoginActivity extends AppCompatActivity {
    private EditText loginEmail;
    private EditText loginPassword;
    private Button login;
    private Button google;
    private Button github;
    private TextView error;
    private TextView signup;


    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private CollectionReference userModels = DB.selectCollection();

    private GoogleSignInOptions gso;
    private GoogleSignInClient googleSignInClient;

    public void initComponent(){
        loginEmail = findViewById(R.id.loginEmail_editText);
        loginPassword = findViewById(R.id.loginPassword_editText);
        google = findViewById(R.id.googleLogin_btn);
        login = findViewById(R.id.login_btn);
        error = findViewById(R.id.error_textView);
        signup = findViewById(R.id.signUp);



    }

    public void initConnection(){
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initComponent();
        initConnection();

        signup.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
            startActivity(intent);
        });

        login.setOnClickListener(view -> {
            Util.hideSoftKeyboard(view);
            login();
        });

        google.setOnClickListener(view -> {
            googleLogin();
        });

        github = findViewById(R.id.gitHubLogin_btn2);

        github.setOnClickListener(view->{
            githubLogin();
        });
    }

    private void githubLogin() {
        Intent intent = new Intent(LoginActivity.this, GithubAuthActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }

    public void login() {
        error.setText("");
        Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
        String email = loginEmail.getText().toString();
        String password = loginPassword.getText().toString();
        Session session = Session.getInstance();

        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
            firebaseAuth.signInWithEmailAndPassword(email, password).onSuccessTask(task -> {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                String currentUserId = user.getUid();

                userModels.whereEqualTo(Util.KEY_USERID, currentUserId)
                        .addSnapshotListener((values, error) -> {
                            assert values != null;
                            if (!values.isEmpty()) {
                                for (QueryDocumentSnapshot value : values) {
                                    String name = value.getString(Util.KEY_FNAME) + " " + value.getString(Util.KEY_LNAME);
                                    session.setUserName(name);
                                    session.setUserId(currentUserId);
                                    session.setEmail(value.getString(Util.KEY_EMAIL));
                                }
                            }
                        });
                startActivity(intent);
                return null;
            }).addOnFailureListener(e -> {
                error.setText("Incorrect Email or Password");
            });
        } else {
            error.setText("Please entre email and password ");
        }
    }



    public void googleLogin() {
        Intent googleSignIn = googleSignInClient.getSignInIntent();
        startActivityForResult(googleSignIn,100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        User user = new User();
        if(requestCode == 100){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                task.getResult(ApiException.class);
                navigateToSecondActivity();

            } catch (ApiException e) {
                Toast.makeText(getApplicationContext(),"Something went wrong", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void navigateToSecondActivity() {
        finish();
        setSession();
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }

    private void setSession(){
        GoogleSignInAccount currUser = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        User user1 = new User();
        Session session = Session.getInstance();
        session.setUserId(currUser.getId());
        session.setEmail(currUser.getEmail());
        session.setUserName(currUser.getGivenName() + " " + currUser.getFamilyName());
    }


}

