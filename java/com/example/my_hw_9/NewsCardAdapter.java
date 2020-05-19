package com.example.my_hw_9;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.Visibility;

import static android.content.Context.MODE_PRIVATE;

public class NewsCardAdapter extends RecyclerView.Adapter<NewsCardAdapter.NewsCardViewHolder> {

    private Context mCtx;
    private List<NewsCard> newsCardList;
    String id;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    View dialogView;


    public NewsCardAdapter(Context mCtx, List<NewsCard> newsCardList) {
        this.mCtx = mCtx;
        this.newsCardList = newsCardList;


    }





    @NonNull
    @Override
    public NewsCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.news_card, null);
        NewsCardViewHolder holder = new NewsCardViewHolder(view);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mCtx.getApplicationContext());
        editor = sharedPreferences.edit();
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final NewsCardViewHolder holder, final int position) {




        final NewsCard newsCard = newsCardList.get(position);
        Log.i("pos", String.valueOf(position));
        holder.headline.setText(newsCard.getHeadline());
        holder.source.setText("| "+newsCard.getSource());
        holder.time.setText(newsCard.getTime());
        Picasso.get().load(newsCard.getImage()).into(holder.imageView);
        String bookmarked = sharedPreferences.getString(newsCardList.get(position).getId(), "default");
        if(bookmarked.equals("default"))
        {
            holder.cardBFill.setVisibility(holder.view.GONE);
            holder.cardBBorder.setVisibility(holder.view.VISIBLE);
        }
        else
        {
            holder.cardBFill.setVisibility(holder.view.VISIBLE);
            holder.cardBBorder.setVisibility(holder.view.GONE);
        }

        //holder.imageView.setImageDrawable(mCtx.getResources().getDrawable(R.drawable.clear));
        holder.view.setOnClickListener(new View.OnClickListener() {  // <--- here
            @Override
            public void onClick(View v) {
                Log.i("W4K","Click-"+position);
                Log.i("w4", newsCardList.get(position).getId());
                Intent intent = new Intent(mCtx,DetailedArticle.class);
                intent.putExtra("id", newsCardList.get(position).getId() );
                mCtx.startActivity(intent);  // <--- here
            }
        });
        holder.view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                final AlertDialog.Builder builder = new AlertDialog.Builder(mCtx);
                LayoutInflater inflater = LayoutInflater.from(mCtx);
                dialogView = inflater.inflate(R.layout.share_dialog, null);
                builder.setView(dialogView);
                AlertDialog alert = builder.create();
                TextView h = (TextView)dialogView.findViewById(R.id.headlineS);
                ImageView i = (ImageView)dialogView.findViewById(R.id.imageViewS);
                final ImageButton bBorder = (ImageButton)dialogView.findViewById(R.id.bookmarkS);
                final ImageButton bFill = (ImageButton)dialogView.findViewById(R.id.bookmark_fill);
                ImageButton twitter = (ImageButton)dialogView.findViewById(R.id.twitter);

                h.setText(newsCardList.get(position).getHeadline());
                Picasso.get().load(newsCardList.get(position).getImage()).into(i);


                String bookmarked = sharedPreferences.getString(newsCardList.get(position).getId(), "default");
                if(!bookmarked.equals("default"))
                {
                    bFill.setVisibility(dialogView.VISIBLE);
                    bBorder.setVisibility(dialogView.GONE);


                }
                else
                {
                    bBorder.setVisibility(dialogView.VISIBLE);
                    bFill.setVisibility(dialogView.GONE);

                }

                bBorder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        toggleShare(position);
                        bFill.setVisibility(dialogView.VISIBLE);
                        bBorder.setVisibility(dialogView.GONE);
                        holder.cardBFill.setVisibility(holder.view.VISIBLE);
                        holder.cardBBorder.setVisibility(holder.view.GONE);
                    }
                });
                bFill.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        toggleShare(position);
                        bFill.setVisibility(dialogView.GONE);
                        bBorder.setVisibility(dialogView.VISIBLE);
                        holder.cardBFill.setVisibility(holder.view.GONE);
                        holder.cardBBorder.setVisibility(holder.view.VISIBLE);
                    }
                });

                twitter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String url = "https://twitter.com/intent/tweet?text="+Uri.encode(newsCard.getHeadline())+"&url="+newsCard.getWebUrl();
                        Intent viewIntent =
                                new Intent("android.intent.action.VIEW",
                                        Uri.parse(url));
                        mCtx.startActivity(viewIntent);
                    }
                });

                alert.show();
                alert.getWindow().setLayout(1000,999);
                Log.i("msg", "yeah this works");

                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return newsCardList.size();
    }

    class NewsCardViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView headline, time, source;
        ImageButton cardBBorder, cardBFill;
        public View view;
        public NewsCardViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            imageView = itemView.findViewById(R.id.image);
            headline = itemView.findViewById(R.id.headlineS);
            time = itemView.findViewById(R.id.time);
            source = itemView.findViewById(R.id.source);
            cardBBorder = itemView.findViewById(R.id.bookmark_border);
            cardBFill = itemView.findViewById(R.id.bookmark_fill);

        }
    }

    public void toggleShare(int position)
    {

        String bookmarked = sharedPreferences.getString(newsCardList.get(position).getId(), "default");
        Log.i("msg", bookmarked);
        String verdict ="";
        if(bookmarked.equals("default"))
        {
            editor.putString(newsCardList.get(position).getId(), "id");
            verdict = "bookmarked";
            Toast.makeText(mCtx, "\""+newsCardList.get(position).getHeadline()+ "\" was added to bookmarks", Toast.LENGTH_SHORT).show();
        }
        else if(bookmarked.equals("id"))
        {
            editor.remove(newsCardList.get(position).getId());
            verdict = "unbookmarked";
            Toast.makeText(mCtx, "\""+newsCardList.get(position).getHeadline()+ "\" was removed from bookmarks", Toast.LENGTH_SHORT).show();
        }
        editor.commit();


    }
}
