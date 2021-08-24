package com.example.recycleapplication.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.recycleapplication.CategoryAdapter;
import com.example.recycleapplication.CategoryModel;
import com.example.recycleapplication.CourseAdapter;
import com.example.recycleapplication.CourseTitleModel;
import com.example.recycleapplication.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SelectCourseTopicActivity extends AppCompatActivity {

    private String title;
    private String id;
    private ImageView courseImage;
    private TextView courseTitle;
    private List<CourseTitleModel> titleList = new ArrayList<>();
    private RecyclerView courseTitlesListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_course_topic);

        courseImage = findViewById(R.id.course_image);
        courseTitle = findViewById(R.id.title);
        courseTitlesListView = findViewById(R.id.course_titles_list);

        Intent intent = getIntent();

        title = intent.getExtras().getString("title");
        id = intent.getExtras().getString("id");

        courseTitle.setText(title);
        Glide.with(this)
                .load(intent.getExtras().getString("imagePath"))
                .into(courseImage);

        // Access a Firestore instance from your Activity
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        //Getting realtime data from firestore
        //Load quizzes data from firestore
        db.collection("courses").document(id).collection("topics").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@NonNull QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.e("Error", "onEvent: ", e);
                    return;
                }
                if (queryDocumentSnapshots != null) {
                    titleList.clear();

                    Log.d("Firestore", "onEvent: ----------------------------------------" );
                    List<DocumentSnapshot> snapshotList = queryDocumentSnapshots.getDocuments();
                    for (DocumentSnapshot snapshot : snapshotList) {
                        Map<String, Object> item = snapshot.getData();
                        String id = item.get("id").toString();
                        String title = item.get("title").toString();
                        Log.d("Firestore data", "onEvent: " + title);
                        titleList.add(new CourseTitleModel(id,title));
                    }
                    displayCourse(titleList);
                } else {
                    Log.e("Firestore", "onEvent: query snapshot was null");
                }
            }
        });

    }

    private void displayCourse(List<CourseTitleModel> course) {
        CourseAdapter adapter = new CourseAdapter(this, course);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false);
        courseTitlesListView.setLayoutManager(gridLayoutManager);
        courseTitlesListView.setAdapter(adapter);
    }
}