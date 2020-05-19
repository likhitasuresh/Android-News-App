package com.example.my_hw_9;

import android.net.Uri;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {
    String keyword;
    RecyclerView recyclerView;
    public SearchFragment(String keyword) {
        this.keyword = keyword;
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        Log.i("sf", "1");
        recyclerView = view.findViewById(R.id.newsListHome);
        //recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        final String url = getString(R.string.backend_url)+"search/guardian/?q="+ Uri.encode(keyword);
        ApiCall obj = new ApiCall(getActivity());
        obj.apiCall(url, recyclerView);
        Log.i("sf", "2");
        final SwipeRefreshLayout mSwipeRefreshLayout;
        mSwipeRefreshLayout = SearchResultsActivity.instance.findViewById(R.id.swiperefresh_items);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to make your refresh action
                // CallYourRefreshingMethod();
                ApiCall.apiCall(url, recyclerView,  mSwipeRefreshLayout);
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        final String url = getString(R.string.backend_url)+"search/guardian/?q="+ Uri.encode(keyword);
        ApiCall obj = new ApiCall(getActivity());
        obj.apiCall(url, recyclerView);
    }
}
