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
import android.widget.Toast;

import com.example.recycleapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;

public class ForgotPasswordActivity extends AppCompatActivity {

    private Button loginButton;
    private EditText userEmail;
    private Button resetPassword;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        loginButton = findViewById(R.id.back_to_login_btn);
        userEmail = findViewById(R.id.et_email);
        resetPassword = findViewById(R.id.send_btn);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = userEmail.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    userEmail.setError("Email cannot empty");
                    userEmail.requestFocus();
                } else {
                    mAuth.sendPasswordResetEmail(userEmail.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d("Reset Password", "Reset Email Sent");
                                        Toast.makeText(ForgotPasswordActivity.this, "Successfully send reset password email",
                                                Toast.LENGTH_SHORT).show();
                                    } else {

                                        String errorCode = ((FirebaseAuthException)task.getException()).getErrorCode().toString();
                                        Log.d("Failed to resetPassword", errorCode);
                                        switch (errorCode) {
                                            case "ERROR_INVALID_EMAIL":
                                                userEmail.setError("The email address is badly formatted");
                                                userEmail.requestFocus();
                                                break;

                                            case "ERROR_USER_NOT_FOUND":
                                                userEmail.setError("There is no user record corresponding to this email");
                                                userEmail.requestFocus();
                                                break;
                                        }
                                    }
                                }
                            });
                }
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity2.class);
                startActivity(intent);
            }
        });
    }
}