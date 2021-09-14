package com.example.recycleapplication.activity;

import static android.graphics.Color.BLACK;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.recycleapplication.Model.QuizQuestionModel;
import com.example.recycleapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;
import java.util.concurrent.TimeUnit;

public class ShowQuizAnswerAcitvity extends AppCompatActivity {

    private TextView quizTitle;
    private LinearLayout quizAnswerListView;
    private String quizID;
    private int counter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_quiz_answer_acitvity);

        quizAnswerListView = findViewById(R.id.quiz_answer_list);
        quizTitle = findViewById(R.id.quiz_title);


        // Access a Firestore instance from your Activity
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        //Get information of the selected quiz
        Intent intent = getIntent();

        quizID = intent.getExtras().getString("id");
        quizTitle.setText(intent.getExtras().getString("title"));

        CollectionReference questionsRef = db.collection("quizzes").document(quizID).collection("questions");

        questionsRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    quizAnswerListView.removeAllViews();
                    counter = 0;
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        counter += 1;
                        Map<String, Object> item = document.getData();
                        String question = item.get("question").toString();
                        String answer = item.get("answer").toString();

                        RelativeLayout questionAnswerBackground = new RelativeLayout(ShowQuizAnswerAcitvity.this);
                        LinearLayout.LayoutParams lpQuestionAnswerBackground = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        lpQuestionAnswerBackground.setMargins(10,15,10,30);
                        questionAnswerBackground.setMinimumHeight(170);
                        questionAnswerBackground.setBackgroundResource(R.drawable.change_password_background);
                        questionAnswerBackground.setLayoutParams(lpQuestionAnswerBackground);

                        TextView noCounter = new TextView(ShowQuizAnswerAcitvity.this);
                        RelativeLayout.LayoutParams rpNoCounter = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        noCounter.setId(View.generateViewId());
                        rpNoCounter.setMargins(30, 20, 0, 0);
                        rpNoCounter.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                        noCounter.setLayoutParams(rpNoCounter);
                        noCounter.setTextColor(BLACK);
                        noCounter.setTextSize(15);
                        noCounter.setTypeface(Typeface.DEFAULT_BOLD);
                        noCounter.setText(counter + ".");

                        TextView quizQuestion = new TextView(ShowQuizAnswerAcitvity.this);
                        RelativeLayout.LayoutParams rpQuizQuestion = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        quizQuestion.setId(View.generateViewId());
                        rpQuizQuestion.addRule(RelativeLayout.RIGHT_OF, noCounter.getId());
                        rpQuizQuestion.setMargins(30, 20, 0, 0);
                        quizQuestion.setLayoutParams(rpQuizQuestion);
                        quizQuestion.setTextColor(BLACK);
                        quizQuestion.setTypeface(Typeface.DEFAULT_BOLD);
                        quizQuestion.setTextSize(15);
                        quizQuestion.setText(question);

                        TextView questionAnswer = new TextView(ShowQuizAnswerAcitvity.this);
                        RelativeLayout.LayoutParams rpQuestionAnswer = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        rpQuestionAnswer.setMargins(30,20,0,20);
                        rpQuestionAnswer.addRule(RelativeLayout.RIGHT_OF, noCounter.getId());
                        rpQuestionAnswer.addRule(RelativeLayout.BELOW, quizQuestion.getId());
                        questionAnswer.setLayoutParams(rpQuestionAnswer);
                        questionAnswer.setTextColor(BLACK);
                        questionAnswer.setTextSize(15);
                        questionAnswer.setText(answer);

                        questionAnswerBackground.addView(noCounter);
                        questionAnswerBackground.addView(quizQuestion);
                        questionAnswerBackground.addView(questionAnswer);

                        quizAnswerListView.addView(questionAnswerBackground);

                    }
                } else {
                    Log.d("User", "Error getting documents: ", task.getException());
                }
            }
        });

    }
}