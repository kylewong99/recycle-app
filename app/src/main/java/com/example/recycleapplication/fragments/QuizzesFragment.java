package com.example.recycleapplication.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.example.recycleapplication.CategoryAdapter;
import com.example.recycleapplication.CategoryModel;
import com.example.recycleapplication.R;
import com.example.recycleapplication.activity.HomeActivity;
import com.example.recycleapplication.activity.LoginActivity;
import com.example.recycleapplication.activity.StartQuizActivity;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class QuizzesFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private RecyclerView catView;
    public static List<CategoryModel> catList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_quizzes, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        catView = (RecyclerView) view.findViewById(R.id.cat_grid);

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
                    catList.clear();
                    Log.d("Firestore", "onEvent: ----------------------------------------" );
                    List<DocumentSnapshot> snapshotList = queryDocumentSnapshots.getDocuments();
                    for (DocumentSnapshot snapshot : snapshotList) {
                        Map<String, Object> item = snapshot.getData();
                        String title = item.get("title").toString();
                        String imagePath = item.get("image").toString();
                        Log.d("Firestore data", "onEvent: " + title);
                        catList.add(new CategoryModel(title,imagePath,0));

                        CategoryAdapter adapter = new CategoryAdapter(getActivity(), catList);
                        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false);
                        catView.setLayoutManager(gridLayoutManager);
                        catView.setAdapter(adapter);
                    }
                } else {
                    Log.e("Firestore", "onEvent: query snapshot was null");
                }
            }
        });

    }

}