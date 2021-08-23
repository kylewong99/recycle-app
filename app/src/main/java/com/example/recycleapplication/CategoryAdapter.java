package com.example.recycleapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.recycleapplication.activity.QuizQuestionsActivity;
import com.example.recycleapplication.activity.QuizScoreActivity;
import com.example.recycleapplication.activity.StartQuizActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private List<CategoryModel> categoryModelList;
    private LayoutInflater inflater;
    private Context context;
    private HashMap<String, String> imgPath = new HashMap<String,String>();
    private final int VERTICAL = 0;
    private final int HORIZONTAL = 1;

    public CategoryAdapter(Context ctx, List<CategoryModel> categoryModelList) {
        this.categoryModelList = categoryModelList;
        this.inflater = LayoutInflater.from(ctx);
        this.context = ctx;
    }

    @NonNull
    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        if (viewType == VERTICAL) {
            view = inflater.inflate(R.layout.vertical_category_item_layout,parent,false);
            return new ViewHolder(view);
        } else {
            view = inflater.inflate(R.layout.horizontal_category_item_layout,parent,false);
            return new ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.ViewHolder holder, int position) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference mStorageReference = storage.getReference().child(categoryModelList.get(position).getImagePath());

        mStorageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String imgURL = uri.toString();
                imgPath.put(Integer.toString(holder.getAdapterPosition()),imgURL);
                Glide.with(context)
                        .load(imgURL)
                        .into(holder.image);
            }
        });

        holder.title.setText(categoryModelList.get(position).getTitle());
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context.getApplicationContext(), StartQuizActivity.class);
                intent.putExtra("title",holder.title.getText().toString());
                intent.putExtra("imagePath",imgPath.get(Integer.toString(holder.getAdapterPosition())));
                context.startActivity(intent);
                Log.d("quizOnClick",holder.title.getText().toString());
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryModelList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (categoryModelList.get(position).getViewType() == 0) {
            return VERTICAL;
        } else {
            return HORIZONTAL;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        ImageView image;
        CardView button;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            image = itemView.findViewById(R.id.image);
            button = itemView.findViewById(R.id.button);
        }
    }
}
