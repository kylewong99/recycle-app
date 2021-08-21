package com.example.recycleapplication;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recycleapplication.activity.StartQuizActivity;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    List<CategoryModel> categoryModelList;
    List<String> titles;
    LayoutInflater inflater;
    Context context;

    public CategoryAdapter(Context ctx, List<CategoryModel> categoryModelList) {
        this.categoryModelList = categoryModelList;
        this.inflater = LayoutInflater.from(ctx);
        this.context = ctx;
    }

    @NonNull
    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.quiz_category_item_layout,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.ViewHolder holder, int position) {
        holder.title.setText(categoryModelList.get(position).getTitle());
        holder.quizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context.getApplicationContext(), StartQuizActivity.class);
                context.startActivity(intent);
                Log.d("quizOnClick",holder.title.getText().toString());
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        CardView quizButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.quiz_title);
            quizButton = itemView.findViewById(R.id.quiz_button);
        }
    }
}
