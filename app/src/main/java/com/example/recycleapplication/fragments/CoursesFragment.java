package com.example.recycleapplication.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.recycleapplication.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

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
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
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

        //
        image = (ImageView) view.findViewById(R.id.test_image);

        FirebaseStorage storage = FirebaseStorage.getInstance();

        mStorageReference = storage.getReference().child("quizzes/quiz2.png");

        mStorageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                imgURL = uri.toString();
                Glide.with(view.getContext())
                        .load(imgURL)
                        .into(image);
            }
        });

//        Glide.with(view.getContext())
//                .load("gs://go-green-3620b.appspot.com/quizzes/quiz2.png")
//                .centerCrop()
//                .placeholder(R.drawable.ic_launcher_foreground)
//                .into(image);

//        try {
//            final File localFile = File.createTempFile("quiz1","jpg");
//            mStorageReference.getFile(localFile)
//                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
//                       @Override
//                       public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
//                           Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
//                           image.setImageBitmap(bitmap);
//                       }
//                    });
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

//        mStorageReference = FirebaseStorage.getInstance().getReference().child("quizzes/quiz1.png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//            @Override
//            public void onSuccess(Uri uri) {
//                Log.d("test", );
//            }
//        });

//        Log.d("test", FirebaseStorage.getInstance().getReference().child("quizzes/quiz1.png").getDownloadUrl().toString());


//        Picasso.get().load(mStorageReference.getDownloadUrl().toString()).into(image);



    }
}