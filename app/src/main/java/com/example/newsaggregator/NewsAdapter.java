package com.example.newsaggregator;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

public class NewsAdapter extends RecyclerView.Adapter<NewsViewHolder> {

    private final NewsInfoActivity newsInfoActivity;
    private final ArrayList<NewsInfo> newsInfos;
    private Picasso picasso;
    private static String webUrI;

    public NewsAdapter(NewsInfoActivity newsInfoActivity, ArrayList<NewsInfo> newsInfos) {
        this.newsInfoActivity=newsInfoActivity;
        this.newsInfos = newsInfos;
    }


    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NewsViewHolder(LayoutInflater.from(parent.getContext()).inflate
                (R.layout.news_entry, parent, false));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        picasso = Picasso.get();
        NewsInfo newsInfo = newsInfos.get(position);
        String date = newsInfo.getDate();
        String [] parts = date.split("T");
        String date1 = parts[0];
        String date2 = parts[1];
        date2 = date2.substring(0,5);
        String [] part2 = date1.split("-");
        String photoUrl = newsInfo.getImageURL();
        String day = part2[2];
        String month = part2[1];
        String year = part2[0];
        String newDate = (day + "/" + month + "/" + year);
        try {
            Date damn =new SimpleDateFormat("dd/MM/yyyy").parse(newDate);
            DateFormat formatter = new SimpleDateFormat("MMM dd, yyyy");
            newDate = formatter.format(damn);
            newDate = newDate + " " + date2;
            holder.aricleTitle.setText(newsInfo.getArticleTitle());
            holder.dateTime.setText(newDate);
            holder.articleAuthor.setText(newsInfo.getAuthor());
            holder.articleText.setText(newsInfo.getText());
            holder.articleText.setMovementMethod(new ScrollingMovementMethod());
            webUrI = newsInfo.getUrl();
            holder.count.setText(position + 1 + " of 10");
            holder.aricleTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //NewsAdapter.this.clickText(webUrI);
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(webUrI));
                    newsInfoActivity.startActivity(intent);
                }
            });



            holder.articleText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //NewsAdapter.this.clickText(webUrI);
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(webUrI));
                    newsInfoActivity.startActivity(intent);
                }
            });

            if (!photoUrl.isEmpty()) {
                picasso.load(photoUrl)
                        .error(R.drawable.missing)
                        .placeholder(R.drawable.placeholder)
                        .into(holder.articleImage);
            }
            else{
                holder.articleImage.setImageResource(R.drawable.image_not_found);

            }

            holder.articleImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //NewsAdapter.this.clickText(webUrI);
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(webUrI));
                    newsInfoActivity.startActivity(intent);
                }
            });
        } catch (ParseException e) {
            e.printStackTrace();
        }


        //2022-01-26
        //need Dec 28, 2020 00:22

    }

    @Override
    public int getItemCount() {
        return newsInfos.size();
    }

    public void clickTitle (String url){
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        System.out.println(webUrI);
        newsInfoActivity.startActivity(intent);


    }


    public void clickText (String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        newsInfoActivity.startActivity(intent);

    }

    public void clickImage (View v) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(webUrI));
        newsInfoActivity.startActivity(intent);

    }
}
