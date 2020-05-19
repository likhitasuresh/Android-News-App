package com.example.my_hw_9;

import android.content.Context;
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

public class NewsApiCall {

    public NewsApiCall()
    {

    }
    public static void apiCall(String url, final RecyclerView recyclerView, final Context ctx)
    {
        apiCall(url, recyclerView,  ctx, null);
    }
    public static void apiCall(String url, final RecyclerView recyclerView, final Context ctx, final SwipeRefreshLayout mSwipeRefreshLayout)
    {
        final List<NewsCard> newsCardList = new ArrayList<>();
        RequestQueue queue = Volley.newRequestQueue(ctx);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.i("url", "hi");//
                            JSONArray valueArray =response.getJSONObject("response").getJSONArray("results");
                            Log.i("newslength", String.valueOf(valueArray.length()));

                            for(int i=0;i<valueArray.length();i++)
                            {
                                JSONObject object = valueArray.getJSONObject(i);

                                try{
                                    newsCardList.add(new NewsCard(object.getString("webTitle"),
                                            DateParser.parse("card",object.getString("webPublicationDate")),
                                            object.getString("sectionName"),
                                            object.getJSONObject("fields").getString("thumbnail"),
                                            object.getString("id"),
                                            object.getString("webUrl")
                                    ));
                                }catch (Exception e)
                                {
                                    Log.i("sf", "inside "+e);
                                }

                            }

                            NewsCardAdapter adapter = new NewsCardAdapter(ctx, newsCardList);
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
                        Log.i("sf", error.toString());

                    }
                });

        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);

    }
}
