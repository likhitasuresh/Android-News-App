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
import android.widget.TextView;

import com.example.my_hw_9.ApiCall;
import com.example.my_hw_9.MainActivity;
import com.example.my_hw_9.NewsApiCall;
import com.example.my_hw_9.NewsCard;
import com.example.my_hw_9.NewsCardAdapter;
import com.example.my_hw_9.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class WorldFragment extends Fragment {

    RecyclerView recyclerView;
    NewsCardAdapter adapter;

    List<NewsCard> newsCardList;

    public WorldFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        newsCardList = new ArrayList<>();
        recyclerView = (RecyclerView)view.findViewById(R.id.newsListHome);
        //recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

//
        Log.i("len", String.valueOf(newsCardList.size()));
        adapter = new NewsCardAdapter(getActivity(), newsCardList);
        recyclerView.setAdapter(adapter);
        ApiCall obj = new ApiCall(getActivity());
        final String url = getString(R.string.backend_url)+"?source=guardian&field=technology";
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
        final String url = getString(R.string.backend_url)+"?source=guardian&field=world";
        ApiCall obj = new ApiCall(getActivity());
        obj.apiCall(url, recyclerView);

    }
}
