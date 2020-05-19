package com.example.my_hw_9;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.my_hw_9.ui.home.NewsFragment;

public class SearchResultsActivity extends AppCompatActivity {

    public static SearchResultsActivity instance;

    public SearchResultsActivity() {
        if(instance==null)
        {
            instance = this;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        TextView header = (TextView)myToolbar.findViewById(R.id.toolbarTextView);

        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(getApplicationContext(),MainActivity.class));
                finish();
            }
        });
        Intent intent=getIntent();
        String keyword= intent.getStringExtra("keyword");
        header.setText("Search results for "+keyword);
        SearchFragment obj = new SearchFragment(keyword);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_search, obj)
                .commit();
    }
}
