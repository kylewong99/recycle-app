package com.example.recycleapplication.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import java.util.ArrayList;
import java.util.List;

public class QuizzesFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private RecyclerView catView;
    public static List<CategoryModel> catList = new ArrayList<>();
    private List<String> titles;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        View view = inflater.inflate(R.layout.fragment_quizzes, container, false);
//        catView = view.findViewById(R.id.cat_grid);
//
//        loadCategories();
//
//        CategoryAdapter adapter = new CategoryAdapter(catList);
//        catView.setAdapter(adapter);

        return inflater.inflate(R.layout.fragment_quizzes, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        catView = (RecyclerView) view.findViewById(R.id.cat_grid);

        loadCategories();
        titles = new ArrayList<>();

        titles.add("1");
        titles.add("2");
        titles.add("3");

        CategoryAdapter adapter = new CategoryAdapter(getActivity(),catList);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),2,GridLayoutManager.VERTICAL,false);
        catView.setLayoutManager(gridLayoutManager);
        catView.setAdapter(adapter);

    }

    private void loadCategories()
    {
        catList.clear();

        catList.add(new CategoryModel("1", "Environmental Issues"));
        catList.add(new CategoryModel("1", "Plastic Pollution"));
        catList.add(new CategoryModel("1", "Environmental Issues"));
        catList.add(new CategoryModel("1", "Environmental Issues"));
        catList.add(new CategoryModel("1", "Environmental Issues"));
        catList.add(new CategoryModel("1", "Environmental Issues"));
    }


}