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

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.recycleapplication.CategoryAdapter;
import com.example.recycleapplication.CategoryModel;
import com.example.recycleapplication.R;
import com.example.recycleapplication.activity.HomeActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
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
import java.util.Collections;
import java.util.Comparator;
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

    private EditText searchBar;

    private TextView popularTitle;
    private TextView coursesTitle;

    private String imgURL;

    private RecyclerView popularCatView;
    private RecyclerView allCoursesCatView;
    private RecyclerView filterView;

    public static List<CategoryModel> popularCatList = new ArrayList<>();
    public static List<CategoryModel> allCoursesCatList = new ArrayList<>();
    public static List<CategoryModel> filteredCatList = new ArrayList<>();

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
        filterView = (RecyclerView) view.findViewById(R.id.filter_view);
        searchBar = (EditText) view.findViewById(R.id.search_cat_bar);
        popularTitle = (TextView) view.findViewById(R.id.most_popular_title);
        coursesTitle = (TextView) view.findViewById(R.id.courses_title);

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
                        int noClicked = Integer.valueOf(item.get("clicked").toString());
                        Log.d("Firestore data", "onEvent: " + title);
                        allCoursesCatList.add(new CategoryModel(title,imagePath,id,noClicked,0));
                    }

                    //Sort courses by number of clicked on descending order
                    Comparator<CategoryModel> clickedComparator = (c1, c2) -> (int) (c1.getNoClicked() - c2.getNoClicked());
                    allCoursesCatList.sort(Collections.reverseOrder(clickedComparator));
                    for (int i = 0; i < allCoursesCatList.size(); i++) {
                        if (i == 5) {
                            break;
                        }
                        String title = allCoursesCatList.get(i).getTitle();
                        String imagePath = allCoursesCatList.get(i).getImagePath();
                        String id = allCoursesCatList.get(i).getId();
                        int noClicked = allCoursesCatList.get(i).getNoClicked();
                        popularCatList.add(new CategoryModel(title,imagePath,id,noClicked,1));
                    }

                    //Sort all courses by title on ascending order
                    Comparator<CategoryModel> titleComparator = (c1, c2) -> c1.getTitle().compareTo(c2.getTitle());
                    allCoursesCatList.sort(titleComparator);

                    displayCourse();
                } else {
                    Log.e("Firestore", "onEvent: query snapshot was null");
                }
            }
        });

        searchBar.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (searchBar.getText().toString().trim().length() == 0) {
                    displayCourse();
                } else {
                    filteredCatList.clear();
                    for (int i = 0; i < allCoursesCatList.size(); i++) {
                        if (allCoursesCatList.get(i).getTitle().toLowerCase().contains(searchBar.getText().toString().toLowerCase())) {
                            filteredCatList.add(allCoursesCatList.get(i));
                        }
                    }
                    displayFilteredCourse(filteredCatList);
                }
            }
        });

    }

    private void displayCourse() {
        popularCatView.setVisibility(View.VISIBLE);
        allCoursesCatView.setVisibility(View.VISIBLE);
        filterView.setVisibility(View.GONE);
        popularTitle.setVisibility(View.VISIBLE);
        coursesTitle.setVisibility(View.VISIBLE);

        CategoryAdapter adapter = new CategoryAdapter(getActivity(), allCoursesCatList);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false);
        allCoursesCatView.setLayoutManager(gridLayoutManager);
        allCoursesCatView.setAdapter(adapter);

        adapter = new CategoryAdapter(getActivity(), popularCatList);
        gridLayoutManager = new GridLayoutManager(getActivity(), 1, GridLayoutManager.HORIZONTAL, false);
        popularCatView.setLayoutManager(gridLayoutManager);
        popularCatView.setAdapter(adapter);
    }

    private void displayFilteredCourse(List<CategoryModel> filteredCatList) {
        popularCatView.setVisibility(View.GONE);
        allCoursesCatView.setVisibility(View.GONE);
        popularTitle.setVisibility(View.GONE);
        coursesTitle.setVisibility(View.GONE);
        filterView.setVisibility(View.VISIBLE);
        filterView.setTranslationZ(100);

        CategoryAdapter adapter = new CategoryAdapter(getActivity(), filteredCatList);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false);
        filterView.setLayoutManager(gridLayoutManager);
        filterView.setAdapter(adapter);
    }

}