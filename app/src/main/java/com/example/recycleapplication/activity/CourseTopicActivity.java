package com.example.recycleapplication.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.MemoryCategory;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.recycleapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
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
    private WebView mWebView;
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
                content.setTextColor(Color.BLACK);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);


                mWebView = new WebView(CourseTopicActivity.this);
                mWebView.setWebViewClient(new WebViewClient());
                mWebView.setWebChromeClient(new MyChrome());
                WebSettings webSettings = mWebView.getSettings();
                webSettings.setJavaScriptEnabled(true);
                webSettings.setAllowFileAccess(true);
                webSettings.setAppCacheEnabled(true);

                LinearLayout.LayoutParams lpWebPlayer = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 550);
                lpWebPlayer.setMargins(0,0,0,100);
                mWebView.setLayoutParams(lpWebPlayer);

                if (savedInstanceState == null) {
                    try {
                        String youtubeURL = item.get("youtubeURL").toString();
                        if (youtubeURL != null) {
                            lp.setMargins(0,0,0,25);
                            content.setLayoutParams(lp);
                            linearLayout.addView(content);

                            mWebView.loadUrl(youtubeURL);
                            linearLayout.addView(mWebView);
                        }
                    } catch (Exception e) {
                        lp.setMargins(0,0,0,100);
                        content.setLayoutParams(lp);
                        linearLayout.addView(content);
                    }
                }


            }
        });

    }

    private class MyChrome extends WebChromeClient {

        private View mCustomView;
        private WebChromeClient.CustomViewCallback mCustomViewCallback;
        protected FrameLayout mFullscreenContainer;
        private int mOriginalOrientation;
        private int mOriginalSystemUiVisibility;

        MyChrome() {}

        public Bitmap getDefaultVideoPoster()
        {
            if (mCustomView == null) {
                return null;
            }
            return BitmapFactory.decodeResource(getApplicationContext().getResources(), 2130837573);
        }

        public void onHideCustomView()
        {
            ((FrameLayout)getWindow().getDecorView()).removeView(this.mCustomView);
            this.mCustomView = null;
            getWindow().getDecorView().setSystemUiVisibility(this.mOriginalSystemUiVisibility);
            setRequestedOrientation(this.mOriginalOrientation);
            this.mCustomViewCallback.onCustomViewHidden();
            this.mCustomViewCallback = null;
        }

        public void onShowCustomView(View paramView, WebChromeClient.CustomViewCallback paramCustomViewCallback)
        {
            if (this.mCustomView != null)
            {
                onHideCustomView();
                return;
            }
            this.mCustomView = paramView;
            this.mCustomView.setBackgroundColor(Color.BLACK);
            this.mOriginalSystemUiVisibility = getWindow().getDecorView().getSystemUiVisibility();
            this.mOriginalOrientation = getRequestedOrientation();
            this.mCustomViewCallback = paramCustomViewCallback;
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            ((FrameLayout)getWindow().getDecorView()).addView(this.mCustomView, new FrameLayout.LayoutParams(-1, -1));
            getWindow().getDecorView().setSystemUiVisibility(3846 | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mWebView.saveState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mWebView.restoreState(savedInstanceState);
    }

}