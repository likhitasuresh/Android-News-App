package com.example.my_hw_9.ui.trending;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.my_hw_9.MainActivity;
import com.example.my_hw_9.NewsApiCall;
import com.example.my_hw_9.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class TrendingFragment extends Fragment {


    LineChart mLineChart;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_trending, container, false);
        mLineChart = root.findViewById(R.id.chart);

        LineData data = null;
        try {
            buildChart("coronavirus", mLineChart);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        final EditText editText = (EditText) root.findViewById(R.id.editText);
        editText.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    Toast.makeText(getActivity(),editText.getText().toString(),Toast.LENGTH_SHORT).show();
                    try {
                        buildChart(editText.getText().toString(), mLineChart);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                    handled = true;
                }
                return handled;
            }
        });
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
        return root;
    }



    public void apiCall(String url, final LineChart mLineChart, final String keyword)
    {
        final ArrayList<Entry> values = new ArrayList<>();
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            Log.i("url", "hi");//
                                JSONArray valueArray =response.getJSONObject("default").getJSONArray("timelineData");
                                JSONObject object;
                                for(int i=0;i<valueArray.length();i++)
                                {
                                    values.add(new Entry(i+1,Integer.valueOf(valueArray.getJSONObject(i).getJSONArray("value").getString(0))));
                                }
                                Log.i("len", String.valueOf(values.size()));
                                LineDataSet chart = new LineDataSet(values, "Trending chart for "+keyword);
                                chart.setAxisDependency(YAxis.AxisDependency.LEFT);
                                chart.setColor(Color.rgb(156,39,177));
                                chart.setLineWidth((float) 1.8);
                                chart.setCircleColor(Color.rgb(63,81,181));
                                chart.setCircleHoleColor(Color.rgb(63,81,181));
                                List<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
                                dataSets.add(chart);
                                LineData data = new LineData(dataSets);
                                mLineChart.setData(data);
                                mLineChart.invalidate();

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
        Log.i("array1", String.valueOf(values.size()));

    }

    public void buildChart(String keyword, LineChart mLineChart) throws UnsupportedEncodingException {
        String url = getString(R.string.google)+"google_trends/?keyword="+ URLEncoder.encode(keyword, "UTF-8");
        apiCall(url, mLineChart, keyword);



    }
}
