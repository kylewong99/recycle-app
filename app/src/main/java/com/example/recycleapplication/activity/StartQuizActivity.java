package com.example.recycleapplication.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.recycleapplication.R;
import com.example.recycleapplication.fragments.QuizzesFragment;

public class StartQuizActivity extends AppCompatActivity {

    private TextView quizTitle;
    private ImageView quizImage;
    private Button startButton;
    private String id;
    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_quiz);

        quizTitle = findViewById(R.id.quiz_title);
        quizImage = findViewById(R.id.quiz_image);
        startButton = findViewById(R.id.start_button);

        //Get information of the selected quiz from QuizzesFragment
        Intent intent = getIntent();

        id = intent.getExtras().getString("id");
        title = intent.getExtras().getString("title");

        //Set quiz title and image
        quizTitle.setText(title);
        Glide.with(this)
                .load(intent.getExtras().getString("imagePath"))
                .into(quizImage);



        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartQuizActivity.this, QuizQuestionsActivity.class);
                intent.putExtra("title", title);
                intent.putExtra("id",id);
                startActivity(intent);
            }
        });

    }
}