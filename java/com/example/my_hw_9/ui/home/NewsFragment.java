package com.example.my_hw_9.ui.home;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.my_hw_9.MainActivity;
import com.example.my_hw_9.NewsApiCall;
import com.example.my_hw_9.NewsCard;
import com.example.my_hw_9.NewsCardAdapter;
import com.example.my_hw_9.R;



import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class NewsFragment extends Fragment {

    SharedPreferences sharedPref;
    RecyclerView recyclerView;
    private ProgressBar spinner;






    public NewsFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




    }

    @Override
    public void onResume() {
        super.onResume();
        String url = getString(R.string.backend_url)+"source=guardian&field=home";
        NewsApiCall.apiCall(url, recyclerView, getActivity());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        spinner=(ProgressBar)view.findViewById(R.id.progressBar);

        recyclerView = (RecyclerView)view.findViewById(R.id.newsListHome);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        sharedPref = getActivity().getPreferences(getActivity().MODE_PRIVATE);

        final SwipeRefreshLayout mSwipeRefreshLayout;
        mSwipeRefreshLayout = MainActivity.instance.findViewById(R.id.swiperefresh_items);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                String url = getString(R.string.backend_url)+"?source=guardian&field=home";
                NewsApiCall.apiCall(url, recyclerView, getActivity(), mSwipeRefreshLayout);
            }
        });
        String url = getString(R.string.backend_url)+"?source=guardian&field=home";
        spinner.setVisibility(view.VISIBLE);
        NewsApiCall.apiCall(url, recyclerView, getActivity());
        Log.i("news", "coming here");
        //spinner.setVisibility(view.GONE);
        return view;
    }


}
