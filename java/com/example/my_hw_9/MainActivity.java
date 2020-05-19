package com.example.my_hw_9;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.my_hw_9.ui.bookmarks.BookmarksFragment;
import com.example.my_hw_9.ui.headlines.HeadlinesFragment;
import com.example.my_hw_9.ui.home.HomeFragment;
import com.example.my_hw_9.ui.home.WeatherFragment;
import com.example.my_hw_9.ui.trending.TrendingFragment;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

//import com.google.android.gms.common.api.Response;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {


    ArrayAdapter<String > adapter;
    Context context;
    List<String> stringList;
    private SearchView searchView;
    private SearchView.SearchAutoComplete   mSearchAutoComplete;

    public static MainActivity instance;

    public MainActivity() {
        if(instance==null)
        {
            instance = this;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_Light_NoActionBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        context = this;

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        loadFragment(new HomeFragment());

        //getting bottom navigation view and attaching the listener
        BottomNavigationView navigation = findViewById(R.id.nav_view);
        navigation.setOnNavigationItemSelectedListener(this);






    }



    @SuppressLint("RestrictedApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search, menu);
        MenuItem searchViewItem = menu.findItem(R.id.app_bar_search);

        searchView = (SearchView) MenuItemCompat.getActionView(searchViewItem);
        mSearchAutoComplete = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        mSearchAutoComplete.setDropDownAnchor(R.id.app_bar_search);
        mSearchAutoComplete.setThreshold(0);

       final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchViewItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
//                if(stringList.contains(query)){
//                    adapter.getFilter().filter(query);
//                }else{
//                    Toast.makeText(MainActivity.this, "No Match found",Toast.LENGTH_LONG).show();
//                }
                return false;

            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //adapter.getFilter().filter(newText);
                makeApiCall(newText);
                return true;
            }
        });

        mSearchAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                searchView.setQuery("",false);
                Log.i("selected" ,adapterView.getItemAtPosition(position).toString());
                Intent intent = new Intent(context,SearchResultsActivity.class);
                intent.putExtra("keyword", adapterView.getItemAtPosition(position).toString());
                context.startActivity(intent);
            }
        });
        return super.onCreateOptionsMenu(menu);
    }




    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;

        switch (item.getItemId()) {
            case R.id.navigation_home:
                fragment = new HomeFragment();
                break;

            case R.id.navigation_headlines:
                fragment = new HeadlinesFragment();
                break;

            case R.id.navigation_trending:
                fragment = new TrendingFragment();
                break;

            case R.id.navigation_bookmarks:
                fragment = new BookmarksFragment();
                break;
        }

        return loadFragment(fragment);
    }

    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.nav_host_fragment, fragment)
                    .commit();
            return true;
        }
        return false;
    }



    private void makeApiCall(String text) {

        String url = "https://api.cognitive.microsoft.com/bing/v7.0/suggestions?q="+text;
                Log.i("ha", text);
            RequestQueue queue = Volley.newRequestQueue(context);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
            (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //parsing logic, please change it as per your requirement
                stringList = new ArrayList<>();
                try {
                        JSONArray jsonArray = response.getJSONArray("suggestionGroups")
                                .getJSONObject(0)
                                .getJSONArray("searchSuggestions");
                        for(int i=0;i<5;i++)
                        {
                            stringList.add(jsonArray.getJSONObject(i).getString("displayText"));
                        }
                        adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, stringList);
                        mSearchAutoComplete.setAdapter(adapter);

                    } catch (JSONException ex) {
                    ex.printStackTrace();
                    Log.i("error", ex.toString());
                }
                Log.i("responses", stringList.toString());

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        })
        {    //this is the part, that adds the header to the request
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Ocp-Apim-Subscription-Key", "274d08877b924b03953cab19cc643715");
                return params;
        }
    };
    queue.add(jsonObjectRequest);
    }

}
