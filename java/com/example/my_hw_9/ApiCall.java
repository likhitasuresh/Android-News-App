package com.example.my_hw_9;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class ApiCall {

    static Context context;
    static SharedPreferences sharedPref;

    public ApiCall(Context context) {

        this.context = context;

    }
    public static void apiCall(String url, final RecyclerView recyclerView)
    {
        apiCall(url, recyclerView, null);
    }
    public static void apiCall(String url, final RecyclerView recyclerView, final SwipeRefreshLayout mSwipeRefreshLayout)
    {
        final List<NewsCard> newsCardList = new ArrayList<>();
        RequestQueue queue = Volley.newRequestQueue(context);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.i("url", "hi");//
                            JSONArray valueArray =response.getJSONObject("response").getJSONArray("results");
                            for(int i=0;i<valueArray.length();i++)
                            {
                                JSONObject object = valueArray.getJSONObject(i);
                                String image;
                                try{
                                    image = object.getJSONObject("blocks")
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
                                try {
                                    newsCardList.add(new NewsCard(
                                            object.getString("webTitle"),
                                            DateParser.parse("card",object.getString("webPublicationDate")),
                                            object.getString("sectionName"),
                                            image,
                                            object.getString("id"),
                                            object.getString("webUrl")

                                    ));
                                } catch (Exception e) {

                                }
                            }
                            ;

                            NewsCardAdapter adapter = new NewsCardAdapter(context, newsCardList);
                            recyclerView.setAdapter(adapter);
                            if(mSwipeRefreshLayout!=null && mSwipeRefreshLayout.isRefreshing()) {
                                mSwipeRefreshLayout.setRefreshing(false);
                            }
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
}
