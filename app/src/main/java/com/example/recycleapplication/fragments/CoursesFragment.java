package com.example.recycleapplication.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.recycleapplication.CategoryAdapter;
import com.example.recycleapplication.CategoryModel;
import com.example.recycleapplication.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CoursesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CoursesFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ImageView image;
    private StorageReference mStorageReference;

    private String imgURL;

    private RecyclerView popularCatView;
    private RecyclerView allCoursesCatView;

    public static List<CategoryModel> popularCatList = new ArrayList<>();
    public static List<CategoryModel> allCoursesCatList = new ArrayList<>();

    public CoursesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CoursesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CoursesFragment newInstance(String param1, String param2) {
        CoursesFragment fragment = new CoursesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_courses, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        popularCatView = (RecyclerView) view.findViewById(R.id.popular_cat_grid);
        allCoursesCatView = (RecyclerView) view.findViewById(R.id.all_courses_cat_grid);

        // Access a Firestore instance from your Activity
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        //Getting realtime data from firestore
        //Load quizzes data from firestore
        db.collection("courses").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@NonNull QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.e("Error", "onEvent: ", e);
                    return;
                }
                if (queryDocumentSnapshots != null) {
                    allCoursesCatList.clear();
                    popularCatList.clear();

                    Log.d("Firestore", "onEvent: ----------------------------------------" );
                    List<DocumentSnapshot> snapshotList = queryDocumentSnapshots.getDocuments();
                    for (DocumentSnapshot snapshot : snapshotList) {
                        Map<String, Object> item = snapshot.getData();
                        String title = item.get("title").toString();
                        String imagePath = item.get("image").toString();
                        String id = item.get("id").toString();
                        Log.d("Firestore data", "onEvent: " + title);
                        allCoursesCatList.add(new CategoryModel(title,imagePath,id,0));
                        popularCatList.add(new CategoryModel(title,imagePath,id,1));

                        CategoryAdapter adapter = new CategoryAdapter(getActivity(), allCoursesCatList);
                        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false);
                        allCoursesCatView.setLayoutManager(gridLayoutManager);
                        allCoursesCatView.setAdapter(adapter);

                        adapter = new CategoryAdapter(getActivity(), popularCatList);
                        gridLayoutManager = new GridLayoutManager(getActivity(), 1, GridLayoutManager.HORIZONTAL, false);
                        popularCatView.setLayoutManager(gridLayoutManager);
                        popularCatView.setAdapter(adapter);
                    }
                } else {
                    Log.e("Firestore", "onEvent: query snapshot was null");
                }
            }
        });

    }
}