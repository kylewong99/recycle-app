package com.example.recycleapplication.activity;

import androidx.annotation.NonNull;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class StartQuizActivity extends AppCompatActivity {

    private TextView quizTitle;
    private TextView highestScore;
    private ImageView quizImage;
    private Button startButton;
    private Button cancelButton;
    private String id;
    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_quiz);

        quizTitle = findViewById(R.id.quiz_title);
        quizImage = findViewById(R.id.quiz_image);
        startButton = findViewById(R.id.start_button);
        cancelButton = findViewById(R.id.cancel_button);
        highestScore = findViewById(R.id.highest_score);

        //Get information of the selected quiz from QuizzesFragment
        Intent intent = getIntent();

        id = intent.getExtras().getString("id");
        title = intent.getExtras().getString("title");

        //Set quiz title and image
        quizTitle.setText(title);
        Glide.with(this)
                .load(intent.getExtras().getString("imagePath"))
                .into(quizImage);

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
                        Log.d("User", document.getId() + " => " + document.getData().get("email"));
                        try {
                            String score = document.getData().get(id + "score").toString();
                            if (score != null) {
                                highestScore.setVisibility(View.VISIBLE);
                                highestScore.setText("Highest Score: " + score + " %");
                                Log.d("User","got score" + score);
                            }
                        } catch (Exception e) {
                            highestScore.setVisibility(View.GONE);
                            Log.d("User","No score");
                        }
                    }
                } else {
                    Log.d("User", "Error getting documents: ", task.getException());
                }
            }
        });

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartQuizActivity.this, QuizQuestionsActivity.class);
                intent.putExtra("title", title);
                intent.putExtra("id",id);
                startActivity(intent);
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StartQuizActivity.super.onBackPressed();
            }
        });

    }
}