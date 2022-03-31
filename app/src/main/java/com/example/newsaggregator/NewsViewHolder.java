package com.example.newsaggregator;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class NewsViewHolder extends RecyclerView.ViewHolder{
    TextView aricleTitle;
    TextView dateTime;
    TextView articleAuthor;
    TextView articleText;
    ImageView articleImage;
    TextView count;

    public NewsViewHolder(@NonNull View itemView) {
        super(itemView);
        aricleTitle = itemView.findViewById(R.id.articleTitle);
        dateTime = itemView.findViewById(R.id.dateTime);
        articleAuthor = itemView.findViewById(R.id.author);
        articleText = itemView.findViewById(R.id.text);
        articleImage = itemView.findViewById(R.id.imageView);
        count = itemView.findViewById(R.id.count);
    }
}
