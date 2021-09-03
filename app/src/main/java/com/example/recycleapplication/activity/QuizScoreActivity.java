package com.example.recycleapplication.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.recycleapplication.R;

public class QuizScoreActivity extends AppCompatActivity {

    private TextView title;
    private TextView result;
    private AppCompatButton homeButton;
    private AppCompatButton retakeButton;
    private String score;
    private String id;
    private String quizTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_score);

        title = findViewById(R.id.quiz_title);
        result = findViewById(R.id.score_tv);
        homeButton = findViewById(R.id.home_button);
        retakeButton = findViewById(R.id.retake_button);

        //Get information of the selected quiz
        Intent intent = getIntent();

        score = intent.getExtras().getString("score");
        id = intent.getExtras().getString("id");
        quizTitle = intent.getExtras().getString("title");

        title.setText(quizTitle);
        result.setText(score + "% Score");

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QuizScoreActivity.this, HomeActivity.class);
                intent.putExtra("selectFragment","quiz");
                startActivity(intent);
            }
        });

        retakeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QuizScoreActivity.this, QuizQuestionsActivity.class);
                intent.putExtra("id",id);
                intent.putExtra("title",quizTitle);
                startActivity(intent);
            }
        });

    }
}