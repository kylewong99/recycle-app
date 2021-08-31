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
import com.bumptech.glide.MemoryCategory;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.recycleapplication.activity.QuizQuestionsActivity;
import com.example.recycleapplication.activity.QuizScoreActivity;
import com.example.recycleapplication.activity.SelectCourseTopicActivity;
import com.example.recycleapplication.activity.StartQuizActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
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

        if (viewType == VERTICAL) {
            View view = inflater.inflate(R.layout.vertical_category_item_layout,parent,false);
            return new ViewHolder(view);
        } else {
            View view = inflater.inflate(R.layout.horizontal_category_item_layout,parent,false);
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
                RequestOptions requestOptions = new RequestOptions();
                requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
                Glide.get(context).setMemoryCategory(MemoryCategory.HIGH);
                Glide.with(context)
                        .load(imgURL)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(holder.image);
            }
        });

        holder.title.setText(categoryModelList.get(position).getTitle());
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String id = categoryModelList.get(holder.getAdapterPosition()).getId();
                String title = holder.title.getText().toString();
                String imagePath = imgPath.get(Integer.toString(holder.getAdapterPosition()));
                Intent intent;

                // Access a Firestore instance from your Activity
                FirebaseFirestore db = FirebaseFirestore.getInstance();

                if (imagePath.toLowerCase().contains("course")) {
                    int noClicked = categoryModelList.get(holder.getAdapterPosition()).getNoClicked();
                    db.collection("courses").document(id).update("clicked",noClicked+1);
                    intent = new Intent(context.getApplicationContext(), SelectCourseTopicActivity.class);
                } else {
                    intent = new Intent(context.getApplicationContext(), StartQuizActivity.class);
                }

                intent.putExtra("title",title);
                intent.putExtra("imagePath",imagePath);
                intent.putExtra("id",id);

                context.startActivity(intent);
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
