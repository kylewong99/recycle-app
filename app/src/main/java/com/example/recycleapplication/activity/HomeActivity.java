package com.example.recycleapplication.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.recycleapplication.R;
import com.example.recycleapplication.fragments.AccountFragment;
import com.example.recycleapplication.fragments.CoursesFragment;
import com.example.recycleapplication.fragments.QuizzesFragment;
import com.example.recycleapplication.fragments.RecyclingFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //loading the default fragment
        loadFragment(new CoursesFragment());

        //getting bottom navigation view and attaching the listener
        BottomNavigationView navigation = findViewById(R.id.bottomNavigationView);
        navigation.setOnNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;

        switch (item.getItemId()) {
            case R.id.courses:
                fragment = new CoursesFragment();
                break;

            case R.id.quizzes:
                fragment = new QuizzesFragment();
                break;

            case R.id.recycling:
                fragment = new RecyclingFragment();
                break;

            case R.id.account:
                fragment = new AccountFragment();
                break;
        }

        return loadFragment(fragment);
    }

    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }
}