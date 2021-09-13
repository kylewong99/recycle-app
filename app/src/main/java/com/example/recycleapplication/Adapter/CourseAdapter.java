package com.example.recycleapplication.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recycleapplication.Model.CourseTitleModel;
import com.example.recycleapplication.R;
import com.example.recycleapplication.activity.CourseTopicActivity;

import java.util.List;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.ViewHolder>{

    private List<CourseTitleModel> courseTitleList;
    private LayoutInflater inflater;
    private Context context;
    private String courseID;

    public CourseAdapter(Context ctx, List<CourseTitleModel> courseTitleList, String courseID) {
        this.courseTitleList = courseTitleList;
        this.inflater = LayoutInflater.from(ctx);
        this.context = ctx;
        this.courseID = courseID;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.course_titles_list_layout,parent,false);
        return new CourseAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.button.setText(courseTitleList.get(position).getTitle());
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("title","ID: " + courseTitleList.get(holder.getAdapterPosition()).getId() +
                        " | Title: " + holder.button.getText().toString());
                Intent intent = new Intent(context.getApplicationContext(), CourseTopicActivity.class);
                intent.putExtra("courseID",courseID);
                intent.putExtra("topicID",courseTitleList.get(holder.getAdapterPosition()).getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return courseTitleList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        AppCompatButton button;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            button = itemView.findViewById(R.id.button);
        }
    }
}
