package com.example.recycleapplication.activity;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.GREEN;
import static android.view.Gravity.CENTER;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.recycleapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class QuizResultActivity extends AppCompatActivity {

    private TextView quizTitle;
    private LinearLayout quizResultListView;
    private String quizID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_result);

        //Get information of the selected quiz
        Intent intent = getIntent();

        // Access a Firestore instance from your Activity
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        quizID = intent.getExtras().getString("quizID");

        quizTitle = findViewById(R.id.quiz_title);
        quizResultListView = findViewById(R.id.quiz_result_list);

        quizTitle.setText(intent.getExtras().getString("quizTitle"));

        // Initialize Firebase Auth
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        String userEmail = mAuth.getCurrentUser().getEmail();

        db.collection("users").whereEqualTo("email", userEmail).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    quizResultListView.removeAllViews();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        try {
                            Integer noAttempt = Integer.valueOf(document.getData().get(quizID + "NoAttempt").toString());

                            for (int i = 0; i < noAttempt; i++) {

                                String attemptDateTime = document.getData().get(quizID + "Attempt" + (i + 1) + "DateTime").toString();
                                String attemptScore = document.getData().get(quizID + "Attempt" + (i + 1) + "Score").toString();

                                RelativeLayout quizResultBackground = new RelativeLayout(QuizResultActivity.this);
                                LinearLayout.LayoutParams lpQuizBackground = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                lpQuizBackground.setMargins(20,30,20,30);
                                quizResultBackground.setMinimumHeight(170);
                                quizResultBackground.setBackgroundResource(R.drawable.change_password_background);
                                quizResultBackground.setLayoutParams(lpQuizBackground);

                                TextView noCounter = new TextView(QuizResultActivity.this);
                                RelativeLayout.LayoutParams rpNoCounter = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                noCounter.setId(View.generateViewId());
                                rpNoCounter.setMargins(30, 20, 0, 0);
                                rpNoCounter.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                                noCounter.setLayoutParams(rpNoCounter);
                                noCounter.setTextColor(BLACK);
                                noCounter.setTextSize(15);
                                noCounter.setTypeface(Typeface.DEFAULT_BOLD);
                                noCounter.setText((i + 1) + ".");

                                TextView dateTime = new TextView(QuizResultActivity.this);
                                RelativeLayout.LayoutParams rpDateTime = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                dateTime.setId(View.generateViewId());
                                rpDateTime.addRule(RelativeLayout.RIGHT_OF, noCounter.getId());
                                rpDateTime.setMargins(30, 20, 0, 0);
                                dateTime.setLayoutParams(rpDateTime);
                                dateTime.setTextColor(BLACK);
                                dateTime.setTypeface(Typeface.DEFAULT_BOLD);
                                dateTime.setTextSize(15);
                                dateTime.setText(attemptDateTime);

                                TextView score = new TextView(QuizResultActivity.this);
                                score.setId(View.generateViewId());

                                ProgressBar progressBar = new ProgressBar(QuizResultActivity.this,null,android.R.attr.progressBarStyleHorizontal);
                                RelativeLayout.LayoutParams rpProgressBar = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                progressBar.setScaleY(4f);
                                progressBar.setId(View.generateViewId());
                                rpProgressBar.setMargins(30,20,30,0);
                                rpProgressBar.addRule(RelativeLayout.LEFT_OF, score.getId());
                                rpProgressBar.addRule(RelativeLayout.RIGHT_OF, noCounter.getId());
                                rpProgressBar.addRule(RelativeLayout.BELOW, dateTime.getId());
                                progressBar.setLayoutParams(rpProgressBar);
                                progressBar.setProgress((int) Double.parseDouble(attemptScore));
                                progressBar.setProgressTintList(ColorStateList.valueOf(Color.rgb(2,205,83)));

                                RelativeLayout.LayoutParams rpScore = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                rpScore.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                                rpScore.addRule(RelativeLayout.BELOW, dateTime.getId());
                                rpScore.setMarginEnd(20);
                                score.setLayoutParams(rpScore);
                                score.setTextColor(BLACK);
                                score.setTypeface(Typeface.DEFAULT_BOLD);
                                score.setTextSize(15);
                                score.setText(attemptScore + " %");

                                quizResultBackground.addView(noCounter);
                                quizResultBackground.addView(dateTime);
                                quizResultBackground.addView(progressBar);
                                quizResultBackground.addView(score);

                                quizResultListView.addView(quizResultBackground);

                            }

                        } catch (Exception e) {

                            ((FrameLayout.LayoutParams) quizResultListView.getLayoutParams()).gravity = CENTER;
//                            quizResultListView.setLayoutParams(lpContentBackground);


                            TextView content = new TextView(QuizResultActivity.this);
                            LinearLayout.LayoutParams lpContent = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                            content.setGravity(Gravity.CENTER);
                            content.setLayoutParams(lpContent);
                            content.setTextColor(BLACK);

                            content.setText("You haven't took this quiz.");

                            quizResultListView.addView(content);
                        }

                    }
                } else {
                    Log.d("User", "Error getting documents: ", task.getException());
                }
            }
        });

    }
}