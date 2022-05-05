package com.example.discovery.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.discovery.GithubAuthActivity;
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

    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private CollectionReference userModels = DB.selectCollection();


    private GoogleSignInOptions gso;
    private GoogleSignInClient googleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();


        loginEmail = findViewById(R.id.loginEmail_editText);
        loginPassword = findViewById(R.id.loginPassword_editText);
        login = findViewById(R.id.login_btn);
        error = findViewById(R.id.error_textView);

        TextView signup = findViewById(R.id.signUp);
        signup.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
            startActivity(intent);
        });

        login.setOnClickListener(view -> {
            Util.hideSoftKeyboard(view);
            login();
        });

        google = findViewById(R.id.googleLogin_btn);

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

        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
            firebaseAuth.signInWithEmailAndPassword(email, password).onSuccessTask(task -> {
//
                FirebaseUser user = firebaseAuth.getCurrentUser();
                assert user != null;
                String currentUserId = user.getUid();

                userModels.whereEqualTo(Util.KEY_USERID, currentUserId)
                        .addSnapshotListener((values, error) -> {
                            if (error != null) {
                            }

                            assert values != null;
                            if (!values.isEmpty()) {
                                for (QueryDocumentSnapshot value : values) {
                                    Session session = Session.getInstance();
                                    String name = value.getString(Util.KEY_FNAME) + " " + value.getString(Util.KEY_LNAME);
                                    session.setUserName(name);
                                    session.setUserId(value.getString(Util.KEY_USERID));
                                }
                            }
                        });
                startActivity(intent);
                return null;
            }).addOnFailureListener(e -> {
                error.setText("Incorrect Email or Password");
                // Toast.makeText(this, "Incorrect Email or Password", Toast.LENGTH_SHORT).show();
            });
        } else {
            error.setText("Please entre email and password ");
//            Toast.makeText(this, "Please entre email and password ", Toast.LENGTH_SHORT).show();
        }
    }

    public void signOut(View view) {
        if (user != null && firebaseAuth != null) {
            firebaseAuth.signOut();
            Session.getInstance().setUserId("");
            Session.getInstance().setUserName("");
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));

        }

    }

    public void googleLogin() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent,1000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1000){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                task.getResult(ApiException.class);
                navigateToSecondActivity();
            } catch (ApiException e) {
                Toast.makeText(getApplicationContext(),"Something went wrong", Toast.LENGTH_SHORT).show();
            }
        }
    }
    void navigateToSecondActivity() {
        finish();
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }
}

