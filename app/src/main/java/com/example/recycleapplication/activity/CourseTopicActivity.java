package com.example.recycleapplication.activity;

import static android.icu.lang.UCharacter.IndicPositionalCategory.BOTTOM;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.MemoryCategory;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.recycleapplication.CategoryAdapter;
import com.example.recycleapplication.CourseTitleModel;
import com.example.recycleapplication.R;
import com.example.recycleapplication.TopicAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CourseTopicActivity extends AppCompatActivity {

    private String courseID;
    private String topicID;
    private TextView title;
    private TextView content;
    private LinearLayout linearLayout;
    private List<String> imageList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_topic);

        title = findViewById(R.id.title);
        linearLayout = findViewById(R.id.linear_layout);

        Intent intent = getIntent();
        FirebaseStorage storage = FirebaseStorage.getInstance();

        courseID = intent.getExtras().getString("courseID");
        topicID = intent.getExtras().getString("topicID");

        // Access a Firestore instance from your Activity
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        //Getting realtime data from firestore
        //Load quizzes data from firestore
        db.collection("courses").document(courseID).collection("topics").document(topicID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                imageList.clear();
                Map<String, Object> item = task.getResult().getData();
                title.setText(item.get("title").toString());
                int noImage = Integer.valueOf(item.get("noImage").toString());
                for (int i = 1; i <= noImage; i++) {
                    imageList.add(item.get("image" + i).toString());
                    String imagePath = item.get("image" + i).toString();

                    ImageView imgView = new ImageView(CourseTopicActivity.this);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    lp.setMargins(0,0,0,25);
                    imgView.setLayoutParams(lp);
                    linearLayout.addView(imgView);

                    StorageReference mStorageReference = storage.getReference().child(imagePath);
                    mStorageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String imgURL = uri.toString();
                            RequestOptions requestOptions = new RequestOptions();
                            requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
                            Glide.get(CourseTopicActivity.this).setMemoryCategory(MemoryCategory.HIGH);
                            Glide.with(CourseTopicActivity.this)
                                    .load(imgURL)
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .into(imgView);

                        }
                    });
                }

                TextView content = new TextView(CourseTopicActivity.this);
                content.setText(item.get("content").toString());
                linearLayout.addView(content);
                Log.d("topic",imageList.get(1));
            }
        });



    }
}