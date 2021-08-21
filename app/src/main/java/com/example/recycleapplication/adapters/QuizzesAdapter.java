package com.example.recycleapplication.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recycleapplication.R;

import java.util.List;

public class QuizzesAdapter extends RecyclerView.Adapter<QuizzesAdapter.ViewHolder>{

    List<String> titles;
//    List<String> images;
    LayoutInflater inflater;

    public QuizzesAdapter(Context ctx, List<String> titles) {
        this.titles = titles;
//        this.images = images;
        this.inflater = LayoutInflater.from(ctx);
    }

    @NonNull
    @Override
    public QuizzesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.fragment_quizzes_grid_layout,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuizzesAdapter.ViewHolder holder, int position) {
        holder.title.setText(titles.get(position));
        holder.buttonLayout.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
                Log.d("quizOnClick",holder.title.getText().toString());
           }
        });
    }

    @Override
    public int getItemCount() {
        return titles.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        CardView buttonLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.textView2);
            buttonLayout = itemView.findViewById(R.id.button_layout);

        }
    }
}
