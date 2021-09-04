package com.example.recycleapplication.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.google.firebase.auth.FirebaseUser;

public class ChangePasswordActivity extends AppCompatActivity {

    private EditText newPassword;
    private EditText confirmPassword;

    private Button saveButton;
    private Button cancelButton;

    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        user = FirebaseAuth.getInstance().getCurrentUser();

        newPassword = findViewById(R.id.new_password);
        confirmPassword = findViewById(R.id.confirm_password);
        saveButton = findViewById(R.id.save_btn);
        cancelButton = findViewById(R.id.cancel_btn);


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePassword();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangePasswordActivity.super.onBackPressed();
            }
        });

    }

    private void changePassword() {
        String password = newPassword.getText().toString();
        String cfmPassword = confirmPassword.getText().toString();

        if (TextUtils.isEmpty(password)) {
            newPassword.setError("Password cannot be empty");
            newPassword.requestFocus();
        } else if (TextUtils.isEmpty(cfmPassword)) {
            confirmPassword.setError("Confirm password cannot be empty");
            confirmPassword.requestFocus();
        } else if (!password.equals(cfmPassword)) {
            confirmPassword.setError("The confirm password does not match password");
            confirmPassword.requestFocus();
        } else {
            user.updatePassword(password)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d("UpdatePassword", "User password updated.");
                                Toast.makeText(ChangePasswordActivity.this, "Password updated successfully",
                                        Toast.LENGTH_SHORT).show();
                                ChangePasswordActivity.super.onBackPressed();
                            }
                        }
                    });
        }

    }
}