package com.example.my_hw_9;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.preference.PreferenceManager;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DetailedArticle extends AppCompatActivity {
    String id;
    TextView titleView, dateView, sourceView, descriptionView, viewFullArticle, toolbarTextView;
    ImageView imageView;
    ImageButton twitter, bFill, bBorder;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String title="", webUrl="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailed_activity);
        final Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(getApplicationContext(),MainActivity.class));
                finish();
            }
        });
        Intent intent=getIntent();
        id= intent.getStringExtra("id");
        titleView = (TextView)findViewById(R.id.titleD);
        dateView = (TextView)findViewById(R.id.dateD);
        sourceView = (TextView)findViewById(R.id.sectionD);
        descriptionView=(TextView)findViewById(R.id.descriptionD);
        viewFullArticle = (TextView)findViewById(R.id.viewFullArticleD);
        imageView = (ImageView)findViewById(R.id.imageD);
        toolbarTextView = (TextView)myToolbar.findViewById(R.id.toolbarTextView);
        twitter = myToolbar.findViewById(R.id.twitter);
        bBorder = myToolbar.findViewById(R.id.bookmark_border);
        bFill = myToolbar.findViewById(R.id.bookmark_fill);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
        editor = sharedPreferences.edit();
        String bookmarked = sharedPreferences.getString(id, "default");
        if(!bookmarked.equals("default"))
        {
            bFill.setVisibility(myToolbar.VISIBLE);
            bBorder.setVisibility(myToolbar.GONE);
        }
        else
        {
            bBorder.setVisibility(myToolbar.VISIBLE);
            bFill.setVisibility(myToolbar.GONE);

        }

        bBorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bFill.setVisibility(myToolbar.VISIBLE);
                bBorder.setVisibility(myToolbar.GONE);
                editor.putString(id, "id");
                Toast.makeText(getApplicationContext(), "\""+titleView.getText()+ "\" was added to bookmarks", Toast.LENGTH_SHORT).show();
                editor.commit();
            }
        });
        bFill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bBorder.setVisibility(myToolbar.VISIBLE);
                bFill.setVisibility(myToolbar.GONE);
                editor.remove(id);
                Toast.makeText(getApplicationContext(), "\""+titleView.getText()+ "\" was removed from bookmarks", Toast.LENGTH_SHORT).show();
                editor.commit();
            }
        });
        twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://twitter.com/intent/tweet?text="+ Uri.encode(title)+"&url="+webUrl;
                Intent viewIntent =
                        new Intent("android.intent.action.VIEW",
                                Uri.parse(url));
                startActivity(viewIntent);
            }
        });
        apiCall(id);



    }

    public void apiCall(String id)
    {
        toggleProgress(true);
        String url = getString(R.string.backend_url)+"detailedArticle/?id="+id+"&source=guardian";
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.i("url", "hi");//
                            JSONObject contentObject =response.getJSONObject("response").getJSONObject("content");
                            title = contentObject.getString("webTitle");
                            String date = DateParser.parse("detailed", contentObject.getString("webPublicationDate"));
                            String section = contentObject.getString("sectionName");
                            JSONArray body = contentObject.getJSONObject("blocks").getJSONArray("body");
                            String description="";
                            for(int i=0;i<body.length();i++)
                            {
                                description += body.getJSONObject(i).getString("bodyHtml");
                            }
                            webUrl = contentObject.getString("webUrl");
                            String image;
                            try{   image = contentObject
                                    .getJSONObject("blocks")
                                    .getJSONObject("main")
                                    .getJSONArray("elements")
                                    .getJSONObject(0)
                                    .getJSONArray("assets")
                                    .getJSONObject(0)
                                    .getString("file");
                            }catch (Exception e)
                            {
                                image = "https://assets.guim.co.uk/images/eada8aa27c12fe2d5afa3a89d3fbae0d/fallback-logo.png";
                            }
                            titleView.setText(title);
                            dateView.setText(date);
                            sourceView.setText(section);
                            descriptionView.setText(Html.fromHtml(description));
                            Picasso.get().load(image).into(imageView);
                            //viewFullArticle.setText("View Full Article");
                            viewFullArticle.setOnClickListener(new View.OnClickListener() {
                                public void onClick(View arg0) {
                                    Intent viewIntent =
                                            new Intent("android.intent.action.VIEW",
                                                    Uri.parse(webUrl));
                                    startActivity(viewIntent);
                                }
                            });
                            toolbarTextView.setText(title);
                            toggleProgress(false);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error

                    }
                });

        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);
    }
    public void toggleProgress(Boolean pro)
    {
        ViewGroup viewGroup = (ViewGroup)findViewById(R.id.layoutC);
        for(int i=0;i<viewGroup.getChildCount();i++)
        {
            if(pro)
            {
                viewGroup.getChildAt(i).setVisibility(View.INVISIBLE);
            }
            else
            {
                viewGroup.getChildAt(i).setVisibility(View.VISIBLE);
            }
        }

        if(pro)
        {
            findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
            findViewById(R.id.textView2).setVisibility(View.VISIBLE);

        }
        else
        {
            findViewById(R.id.progressBar).setVisibility(View.INVISIBLE);
            findViewById(R.id.textView2).setVisibility(View.INVISIBLE);

        }
    }
}
