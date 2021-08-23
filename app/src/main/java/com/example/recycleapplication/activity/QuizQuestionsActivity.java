package com.example.recycleapplication.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.recycleapplication.CategoryModel;
import com.example.recycleapplication.QuizQuestionModel;
import com.example.recycleapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class QuizQuestionsActivity extends AppCompatActivity {

    private String id;
    private String quizTitle;
    private int counter = 1;
    private int totalQuestions;
    private Button submitButton;
    private TextView title;
    private TextView questionNo;
    private TextView question;
    private TextView optionA;
    private TextView optionB;
    private TextView optionC;
    private TextView optionD;
    public static List<QuizQuestionModel> questionList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_questions);

        title = findViewById(R.id.title);
        questionNo = findViewById(R.id.no_of_questions);
        submitButton = findViewById(R.id.submit_button);
        question = findViewById(R.id.question);
        optionA = findViewById(R.id.optionA);
        optionB = findViewById(R.id.optionB);
        optionC = findViewById(R.id.optionC);
        optionD = findViewById(R.id.optionD);

        // Access a Firestore instance from your Activity
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        //Get information of the selected quiz
        Intent intent = getIntent();

        id = intent.getExtras().getString("id");
        quizTitle = intent.getExtras().getString("title");
        title.setText(quizTitle);

        Log.d("id",id);

        CollectionReference questionsRef = db.collection("quizzes").document(id).collection("questions");

        questionsRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    questionList.clear();
                    for (QueryDocumentSnapshot document: task.getResult()) {
                        Map<String, Object> item = document.getData();
                        String question = item.get("question").toString();
                        String optionA = item.get("optionA").toString();
                        String optionB = item.get("optionB").toString();
                        String optionC = item.get("optionC").toString();
                        String optionD = item.get("optionD").toString();
                        String answer = item.get("answer").toString();
                        questionList.add(new QuizQuestionModel(quizTitle,question,optionA,optionB,optionC,optionD,answer));
                    }
                    counter = 1;
                    totalQuestions = questionList.size();
                    questionNo.setText(Integer.toString(counter) + "/" + Integer.toString(totalQuestions));
                    displayQuestion();
                }
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counter += 1;
                displayQuestion();
            }
        });
    }

    private void displayQuestion() {
        if (counter <= totalQuestions) {
            QuizQuestionModel questionInfo = questionList.get(counter-1);
            questionNo.setText(Integer.toString(counter) + "/" + Integer.toString(totalQuestions));
            question.setText(questionInfo.getQuestion());
            optionA.setText(questionInfo.getOptionA());
            optionB.setText(questionInfo.getOptionB());
            optionC.setText(questionInfo.getOptionC());
            optionD.setText(questionInfo.getOptionD());
        } else {
            Log.d("questionEnd","End of questions");
        }
    }

}