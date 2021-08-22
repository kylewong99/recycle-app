package com.example.recycleapplication.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.recycleapplication.R;
import com.example.recycleapplication.fragments.QuizzesFragment;

public class StartQuizActivity extends AppCompatActivity {

    private TextView quizTitle;
    private ImageView quizImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_quiz);

        quizTitle = findViewById(R.id.quiz_title);
        quizImage = findViewById(R.id.quiz_image);

        //Get information of the selected quiz from QuizzesFragment
        Intent intent = getIntent();

        //Set quiz title and image
        quizTitle.setText(intent.getExtras().getString("title"));
        Glide.with(this)
                .load(intent.getExtras().getString("imagePath"))
                .into(quizImage);

    }
}