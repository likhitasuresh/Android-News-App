package com.example.my_hw_9.ui.headlines;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.my_hw_9.ApiCall;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class BusinessFragment extends Fragment {

    RecyclerView recyclerView;

    public BusinessFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, container, false);

       recyclerView = (RecyclerView) view.findViewById(R.id.newsListHome);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        final String url = getString(R.string.backend_url)+"?source=guardian&field=business";
        ApiCall obj = new ApiCall(getActivity());
        obj.apiCall(url, recyclerView);
        final SwipeRefreshLayout mSwipeRefreshLayout;
        mSwipeRefreshLayout = MainActivity.instance.findViewById(R.id.swiperefresh_items);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to make your refresh action
                // CallYourRefreshingMethod();
                NewsApiCall.apiCall(url, recyclerView, getActivity(), mSwipeRefreshLayout);
            }
        });
        return view;

    }
    @Override
    public void onResume() {
        super.onResume();
        final String url = getString(R.string.backend_url)+"?source=guardian&field=business";
        ApiCall obj = new ApiCall(getActivity());
        obj.apiCall(url, recyclerView);

    }

}
