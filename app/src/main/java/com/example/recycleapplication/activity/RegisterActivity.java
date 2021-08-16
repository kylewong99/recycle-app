package com.example.recycleapplication.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.recycleapplication.R;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private AccessTokenTracker accessTokenTracker;

    // Email and password variables
    private EditText username;
    private EditText userEmail;
    private EditText userPassword;
    private EditText userConfirmPassword;
    private Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = findViewById(R.id.et_username);
        userEmail = findViewById(R.id.et_email);
        userPassword = findViewById(R.id.et_password);
        userConfirmPassword = findViewById(R.id.et_confirm_password);
        register = findViewById(R.id.register_btn);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        register.setOnClickListener(view -> {
            createUser();
        });

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user!=null) {
                    updateUI(user);
                }else {
                    updateUI(null);
                }
            }
        };

        accessTokenTracker = new AccessTokenTracker() {
            @Override
            public void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                if(currentAccessToken == null) {
                    mAuth.signOut();
                }
            }
        };

        TextView textView = (TextView) findViewById(R.id.back_to_login);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity2.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();

        if(authStateListener != null) {
            mAuth.removeAuthStateListener(authStateListener);
        }
    }

    private void createUser() {
        String email = userEmail.getText().toString();
        String password = userPassword.getText().toString();
        String confirmPassword = userConfirmPassword.getText().toString();
        String userName = username.getText().toString();

        if(TextUtils.isEmpty(email)) {
            userEmail.setError("Email cannot empty");
            userEmail.requestFocus();
        }else if (TextUtils.isEmpty(password)) {
            userPassword.setError("Password cannot be empty");
            userPassword.requestFocus();
        } else if(TextUtils.isEmpty(confirmPassword)) {
            userConfirmPassword.setError("Confirm password cannot be empty");
            userConfirmPassword.requestFocus();
        } else if (!password.equals(confirmPassword)) {
            userConfirmPassword.setError("The confirm password does not match password.");
            userConfirmPassword.requestFocus();
        } else{
            mAuth.createUserWithEmailAndPassword(email,password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("", "createUserWithEmail:success");
                                Toast.makeText(RegisterActivity.this, "User Registered Successful.",
                                        Toast.LENGTH_SHORT).show();
                                FirebaseUser user = mAuth.getCurrentUser();
                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(userName)
                                        .build();

                                user.updateProfile(profileUpdates)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Log.d("Update username", "User profile updated.");
                                                }
                                            }
                                        });
                            updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("", "createUserWithEmail:failure", task.getException());
                                Toast.makeText(RegisterActivity.this, "Registration Error: " +task.getException().getMessage(),
                                        Toast.LENGTH_SHORT).show();
//                            updateUI(null);
                            }
                        }
                    });
        }
    }

    private void updateUI(FirebaseUser fUser) {

        if (fUser != null) {
            String personName = fUser.getDisplayName();
            String personEmail = fUser.getEmail();

            Toast.makeText(RegisterActivity.this, personName + personEmail, Toast.LENGTH_SHORT).show();

            // Testing sign in
//            mAuth.signOut();
        }
    }
}