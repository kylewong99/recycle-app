package com.example.recycleapplication.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.recycleapplication.CategoryModel;
import com.example.recycleapplication.R;
import com.example.recycleapplication.fragments.AccountFragment;
import com.example.recycleapplication.fragments.CoursesFragment;
import com.example.recycleapplication.fragments.QuizzesFragment;
import com.example.recycleapplication.fragments.RecyclingFragment;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HomeActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth mAuth;

    private FirebaseAuth.AuthStateListener authStateListener;
    private AccessTokenTracker accessTokenTracker;
    private GoogleSignInClient mGoogleSignInClient;
    private Button googleSignInButton;

    private final Fragment courseFragment = new CoursesFragment();
    private final Fragment quizzesFragment = new QuizzesFragment();
    private final Fragment recyclingFragment = new RecyclingFragment();
    private final Fragment accountFragment = new AccountFragment();
    private final FragmentManager fm = getSupportFragmentManager();
    Fragment active = courseFragment;
    Bundle bundle = new Bundle();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        bundle.putString("username", mAuth.getCurrentUser().getDisplayName());
        bundle.putString("email", mAuth.getCurrentUser().getEmail());
        accountFragment.setArguments(bundle);

        //getting bottom navigation view and attaching the listener
        BottomNavigationView navigation = findViewById(R.id.bottomNavigationView);

        fm.beginTransaction().add(R.id.fragment_container, accountFragment, "4").hide(accountFragment).commit();
        fm.beginTransaction().add(R.id.fragment_container, recyclingFragment, "3").hide(recyclingFragment).commit();
        fm.beginTransaction().add(R.id.fragment_container,quizzesFragment,"2").hide(quizzesFragment).commit();
        fm.beginTransaction().add(R.id.fragment_container,courseFragment,"1").commit();

        navigation.setOnNavigationItemSelectedListener(this);

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    Log.d("Go to Login", "Logout and go to login");
                    Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        };

        accessTokenTracker = new AccessTokenTracker() {
            @Override
            public void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                if (currentAccessToken == null) {
                    mAuth.signOut();
                }
            }
        };
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.courses:
                fm.beginTransaction().hide(active).show(courseFragment).commit();
                active = courseFragment;
                return true;

            case R.id.quizzes:
                fm.beginTransaction().hide(active).show(quizzesFragment).commit();
                active = quizzesFragment;
                return true;

            case R.id.recycling:
                fm.beginTransaction().hide(active).show(recyclingFragment).commit();
                active = recyclingFragment;
                return true;

            case R.id.account:
                fm.beginTransaction().hide(active).show(accountFragment).commit();
                active = accountFragment;
                return true;
        }
        return false;
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (authStateListener != null) {
            mAuth.removeAuthStateListener(authStateListener);
        }
    }

    public void signOut() {
        mAuth.signOut();
        mGoogleSignInClient.signOut();
    }

}