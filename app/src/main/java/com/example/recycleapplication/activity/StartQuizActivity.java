package com.example.recycleapplication.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.recycleapplication.R;
import com.example.recycleapplication.fragments.QuizzesFragment;

public class StartQuizActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_quiz);

        //int cat_index = getIntent().getIntExtra("CAT_INDEX", 0);
    }
}