package com.example.recycleapplication.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.recycleapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

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

        // Access a Firestore instance from your Activity
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Initialize Firebase Auth
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        String userEmail = mAuth.getCurrentUser().getEmail();

        db.collection("users").whereEqualTo("email",userEmail).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String userID = document.getData().get("id").toString();
                        try {
                            String highestScore = document.getData().get(id + "score").toString();
                            if (highestScore != null) {
                                if (Double.valueOf(score) > Double.valueOf(highestScore)) {
                                    db.collection("users").document(userID).update(id + "score", score);
                                }
                                Log.d("Result","New high score" + score);
                            }
                        } catch (Exception e) {
                            db.collection("users").document(userID).update(id + "score", score);
                        }
                    }
                } else {
                    Log.d("User", "Error getting documents: ", task.getException());
                }
            }
        });

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