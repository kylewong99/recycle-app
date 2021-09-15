package com.example.recycleapplication.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class QuizQuestionsActivity extends AppCompatActivity {

    private String id;
    private String quizTitle;
    private String selectedAnswer;
    private CountDownTimer countDownTimer;
    private int noQuestion = 1;
    private int totalQuestions;
    private int noCorrectQuestion;
    private Button submitButton;
    private TextView timer;
    private TextView title;
    private TextView questionNo;
    private TextView question;
    private TextView optionA;
    private TextView optionB;
    private TextView optionC;
    private TextView optionD;
    private RelativeLayout optionAbutton;
    private RelativeLayout optionBbutton;
    private RelativeLayout optionCbutton;
    private RelativeLayout optionDbutton;

    private static List<QuizQuestionModel> questionList = new ArrayList<>();
    private static List<RelativeLayout> buttonList = new ArrayList<>();
    private static List<TextView> optionList = new ArrayList<>();

    private final int CHOSEN_A = 0;
    private final int CHOSEN_B = 1;
    private final int CHOSEN_C = 2;
    private final int CHOSEN_D = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_questions);

        timer = findViewById(R.id.timer);
        title = findViewById(R.id.title);
        questionNo = findViewById(R.id.no_of_questions);
        submitButton = findViewById(R.id.submit_button);
        question = findViewById(R.id.question);
        optionA = findViewById(R.id.optionA);
        optionB = findViewById(R.id.optionB);
        optionC = findViewById(R.id.optionC);
        optionD = findViewById(R.id.optionD);
        optionAbutton = findViewById(R.id.optionA_box);
        optionBbutton = findViewById(R.id.optionB_box);
        optionCbutton = findViewById(R.id.optionC_box);
        optionDbutton = findViewById(R.id.optionD_box);

        // Access a Firestore instance from your Activity
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        //Get information of the selected quiz
        Intent intent = getIntent();

        id = intent.getExtras().getString("id");
        quizTitle = intent.getExtras().getString("title");
        title.setText(quizTitle);
        timer.setText("");

        selectedAnswer = "";
        noCorrectQuestion = 0;
        buttonList.clear();
        optionList.clear();

        buttonList.add(optionAbutton);
        buttonList.add(optionBbutton);
        buttonList.add(optionCbutton);
        buttonList.add(optionDbutton);

        optionList.add(optionA);
        optionList.add(optionB);
        optionList.add(optionC);
        optionList.add(optionD);

        CollectionReference questionsRef = db.collection("quizzes").document(id).collection("questions");

        questionsRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    questionList.clear();

                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Map<String, Object> item = document.getData();
                        String question = item.get("question").toString();
                        String optionA = item.get("optionA").toString();
                        String optionB = item.get("optionB").toString();
                        String optionC = item.get("optionC").toString();
                        String optionD = item.get("optionD").toString();
                        String answer = item.get("answer").toString();
                        questionList.add(new QuizQuestionModel(quizTitle, question, optionA, optionB, optionC, optionD, answer));
                    }
                    noQuestion = 1;
                    totalQuestions = questionList.size();
                    questionNo.setText(Integer.toString(noQuestion) + "/" + Integer.toString(totalQuestions));
                    displayQuestion();


                    countDownTimer = new CountDownTimer(30000 * questionList.size(), 1000) {

                        public void onTick(long millisUntilFinished) {
                            timer.setText("Time Remaining: " + String.format("%02dm: %02ds", TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                                    TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
                        }

                        public void onFinish() {
                            calculateScore();
                        }
                    };
                    countDownTimer.start();
                }
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer();
            }
        });

        optionAbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayChosenAnswer(CHOSEN_A);
            }
        });

        optionBbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayChosenAnswer(CHOSEN_B);
            }
        });

        optionCbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayChosenAnswer(CHOSEN_C);
            }
        });

        optionDbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayChosenAnswer(CHOSEN_D);
            }
        });

    }

    private void displayQuestion() {
        if (noQuestion <= totalQuestions) {
            removeChosenAnswer();
            QuizQuestionModel questionInfo = questionList.get(noQuestion - 1);
            questionNo.setText(Integer.toString(noQuestion) + "/" + Integer.toString(totalQuestions));
            question.setText(questionInfo.getQuestion());
            optionA.setText(questionInfo.getOptionA());
            optionB.setText(questionInfo.getOptionB());
            optionC.setText(questionInfo.getOptionC());
            optionD.setText(questionInfo.getOptionD());
        } else {
            Log.d("questionEnd", "End of questions");
            calculateScore();
        }
    }

    private void displayChosenAnswer(int chosenAnswer) {
        for (int i = 0; i < buttonList.size(); i++) {
            if (i == chosenAnswer) {
                selectedAnswer = optionList.get(i).getText().toString();
                buttonList.get(i).setBackgroundResource(R.drawable.selected_option_button_style);
            } else {
                buttonList.get(i).setBackgroundResource(R.drawable.options_button_style);
            }
        }
    }

    private void removeChosenAnswer() {
        for (int i = 0; i < buttonList.size(); i++) {
            buttonList.get(i).setBackgroundResource(R.drawable.options_button_style);
        }
    }

    private void checkAnswer() {
        if (!selectedAnswer.isEmpty()) {
            if (selectedAnswer.equals(questionList.get(noQuestion - 1).getAnswer())) {
                noCorrectQuestion += 1;
                Log.d("answer", "Correct Answer");
            } else {
                Log.d("answer", "Wrong Answer");
            }
            selectedAnswer = "";
            noQuestion += 1;
            displayQuestion();
        } else {
            Log.d("answer", "No Answer Selected");
        }
    }

    private void calculateScore() {
        countDownTimer.cancel();
        Double score = ((noCorrectQuestion * 1.0) / (totalQuestions * 1.0)) * 100;
        //Format score to two decimal points
        DecimalFormat df = new DecimalFormat("0.00");

        Intent intent = new Intent(QuizQuestionsActivity.this, QuizScoreActivity.class);
        intent.putExtra("score", df.format(score));
        intent.putExtra("title", title.getText().toString());
        intent.putExtra("id", id);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Really Exit?")
                .setMessage("Are you sure you want to exit?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        countDownTimer.cancel();
                        QuizQuestionsActivity.super.onBackPressed();
                    }
                }).create().show();
    }

}