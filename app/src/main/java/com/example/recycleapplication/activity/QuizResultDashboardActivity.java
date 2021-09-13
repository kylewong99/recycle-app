package com.example.recycleapplication.activity;

import static android.graphics.Color.BLACK;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.recycleapplication.Model.QuizResultModel;
import com.example.recycleapplication.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class QuizResultDashboardActivity extends AppCompatActivity {

    private TextView title;
    private LinearLayout quizView;
    private List<QuizResultModel> quizList = new ArrayList<>();
    private Integer counter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_result_dashboard);

        title = findViewById(R.id.title);
        quizView = findViewById(R.id.quiz_list);

        // Access a Firestore instance from your Activity
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        //Getting realtime data from firestore
        //Load quizzes data from firestore
        db.collection("quizzes").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@NonNull QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.e("Error", "onEvent: ", e);
                    return;
                }
                if (queryDocumentSnapshots != null) {
                    quizList.clear();
                    quizView.removeAllViews();

                    Log.d("Firestore", "onEvent: ----------------------------------------");
                    List<DocumentSnapshot> snapshotList = queryDocumentSnapshots.getDocuments();

                    counter = 0;
                    for (DocumentSnapshot snapshot : snapshotList) {
                        counter += 1;
                        Map<String, Object> item = snapshot.getData();
                        String quizTitle = item.get("title").toString();
                        String quizID = item.get("id").toString();
                        Log.d("Firestore data", "onEvent: " + quizTitle);

                        RelativeLayout quizBackground = new RelativeLayout(QuizResultDashboardActivity.this);
                        LinearLayout.LayoutParams lpQuizBackground = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        lpQuizBackground.setMargins(20,10,20,30);
                        quizBackground.setMinimumHeight(140);
                        quizBackground.setBackgroundResource(R.drawable.change_password_background);
                        quizBackground.setLayoutParams(lpQuizBackground);

                        TextView noCounter = new TextView(QuizResultDashboardActivity.this);
                        RelativeLayout.LayoutParams rpNoCounter = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        noCounter.setId(View.generateViewId());
                        rpNoCounter.setMarginStart(20);
                        rpNoCounter.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                        rpNoCounter.addRule(RelativeLayout.CENTER_VERTICAL);
                        noCounter.setLayoutParams(rpNoCounter);
                        noCounter.setTextColor(BLACK);
                        noCounter.setTypeface(Typeface.DEFAULT_BOLD);
                        noCounter.setText(counter.toString() + ".");

                        ImageView nextIcon = new ImageView(QuizResultDashboardActivity.this);
                        nextIcon.setId(View.generateViewId());

                        TextView title = new TextView(QuizResultDashboardActivity.this);
                        RelativeLayout.LayoutParams rpTitle = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        rpTitle.addRule(RelativeLayout.RIGHT_OF, noCounter.getId());
                        rpTitle.addRule(RelativeLayout.LEFT_OF, nextIcon.getId());
                        rpTitle.addRule(RelativeLayout.CENTER_VERTICAL);
                        rpTitle.setMargins(15, 0, 0, 0);
                        title.setLayoutParams(rpTitle);
                        title.setTextColor(BLACK);
                        title.setTypeface(Typeface.DEFAULT_BOLD);
                        title.setTextSize(15);
                        title.setText(quizTitle);

                        RelativeLayout.LayoutParams rpIcon = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        rpIcon.setMargins(0, 0, 15, 0);
                        rpIcon.addRule(RelativeLayout.CENTER_VERTICAL);
                        rpIcon.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                        nextIcon.setLayoutParams(rpIcon);
                        nextIcon.setImageResource(R.drawable.ic_next);

                        quizBackground.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Log.d("Dashboard",quizTitle);
                                Intent intent = new Intent(QuizResultDashboardActivity.this, QuizResultActivity.class);
                                intent.putExtra("quizID",quizID);
                                intent.putExtra("quizTitle", quizTitle);
                                startActivity(intent);
                            }
                        });

                        quizBackground.addView(noCounter);
                        quizBackground.addView(title);
                        quizBackground.addView(nextIcon);

                        quizView.addView(quizBackground);
                    }
                } else {
                    Log.e("Firestore", "onEvent: query snapshot was null");
                }
            }
        });

//        ImageView imgView = new ImageView(QuizResultDashboardActivity.this);
//        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        lp.setMargins(0, 0, 0, 25);
//        imgView.setLayoutParams(lp);
//        quizView.addView(imgView);


    }
}