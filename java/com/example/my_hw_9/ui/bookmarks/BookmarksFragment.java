package com.example.my_hw_9.ui.bookmarks;

import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.my_hw_9.ApiCall;
import com.example.my_hw_9.BookmarksAdapter;
import com.example.my_hw_9.DateParser;
import com.example.my_hw_9.MainActivity;
import com.example.my_hw_9.NewsCard;
import com.example.my_hw_9.NewsCardAdapter;
import com.example.my_hw_9.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BookmarksFragment extends Fragment {

    private BookmarksViewModel mViewModel;
    List<NewsCard> newsCardList;
    NewsCard newsCard;
    int bookmarks;
    View view;
    Boolean stop;
    TextView no;

    public BookmarksFragment () {

        newsCardList = new ArrayList<>();


    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.bookmarks_fragment, container, false);
        stop = false;
        no = view.findViewById(R.id.no_bookmarks);
        no.setVisibility(view.GONE);
        getBookmarks(view);
        final SwipeRefreshLayout mSwipeRefreshLayout;
        mSwipeRefreshLayout = MainActivity.instance.findViewById(R.id.swiperefresh_items);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to make your refresh action
                // CallYourRefreshingMethod();
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(mSwipeRefreshLayout.isRefreshing()) {
                            mSwipeRefreshLayout.setRefreshing(false);
                        }
                    }
                }, 1000);
            }});
        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        if(stop==true)
            getBookmarks(view);
        stop=false;

    }

    @Override
    public void onPause() {
        super.onPause();
        stop = true;

    }


    public void getBookmarks(View view)
    {
       newsCardList.clear();
       bookmarks = 0;
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        Map<String, ?> map = sharedPreferences.getAll();
        bookmarks = map.size();
        if (bookmarks==0)
            no.setVisibility(view.VISIBLE);
        for (final String name : map.keySet()) {
            apiCall(name, view);
            Log.i("id", name);
        }




    }

    public void apiCall(final String id, final View view)
    {

        String url = getString(R.string.backend_url)+"detailedArticle/?id="+id+"&source=guardian";
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("url", "hi");//
                        try{
                            JSONObject contentObject =response.getJSONObject("response").getJSONObject("content");
                            String title = contentObject.getString("webTitle");
                            String date = DateParser.parse("bookmark",contentObject.getString("webPublicationDate"));
                            String section = contentObject.getString("sectionName");
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
                            String url = contentObject.getString("webUrl");
                            newsCard = new NewsCard(title, date, section, image, id, url);
                            Log.i("headline", title);

                            newsCardList.add(newsCard);
                            bookmarks--;

                            if(bookmarks<=0)
                            {
                                Log.i("you", "kill me");
                                RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.bookmarksRecyclerView);
                                recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
                                BookmarksAdapter adapter = new BookmarksAdapter(getActivity(),newsCardList);
                                recyclerView.setAdapter(adapter);

                            }

                        }
                        catch(Exception e)
                        {
                            Log.i("gimme", "some attention"+e);
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

    public void placeholder()
    {
        getBookmarks(view);
    }

}
