package com.example.recycleapplication;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;
import java.util.List;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.ViewHolder>{

    private List<CourseTitleModel> courseTitleList;
    private LayoutInflater inflater;
    private Context context;

    public CourseAdapter(Context ctx, List<CourseTitleModel> courseTitleList) {
        this.courseTitleList = courseTitleList;
        this.inflater = LayoutInflater.from(ctx);
        this.context = ctx;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.course_titles_list_layout,parent,false);
        return new CourseAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.title.setText(courseTitleList.get(position).getTitle());
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("title","ID: " + courseTitleList.get(holder.getAdapterPosition()).getId() +
                        " | Title: " + holder.title.getText().toString());
            }
        });
    }

    @Override
    public int getItemCount() {
        return courseTitleList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        ConstraintLayout button;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            button = itemView.findViewById(R.id.button);
        }
    }
}
