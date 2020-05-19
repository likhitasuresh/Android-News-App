package com.example.my_hw_9.ui.home;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Looper;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.my_hw_9.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


public class WeatherFragment extends Fragment {

    private HomeViewModel homeViewModel;
    String summary, temp, city, state;
    CardView weatherCard;
    TextView tempText;
    TextView summaryText;
    TextView cityText;
    TextView stateText;
    ImageView imageView;
    FusedLocationProviderClient mFusedLocationClient;
    double MyLat=0.0, MyLong=0.0;
    int PERMISSION_ID = 44;
    View view;


    public WeatherFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_weather, container, false);

        weatherCard = view.findViewById(R.id.weatherCard);
        tempText = view.findViewById(R.id.tempText);
        summaryText = view.findViewById(R.id.summaryText);
        cityText = view.findViewById(R.id.cityText);
        stateText = view.findViewById(R.id.stateText);
        imageView = view.findViewById(R.id.imageViewS);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        getLastLocation();
        return view;
    }



    @SuppressLint("MissingPermission")
    private void getLastLocation(){
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.getLastLocation().addOnCompleteListener(
                        new OnCompleteListener<Location>() {
                            @Override
                            public void onComplete(@NonNull Task<Location> task) {
                                Location location = task.getResult();
                                if (location == null) {
                                    requestNewLocationData();
                                } else {
                                    MyLat = location.getLatitude();
                                    MyLong = location.getLongitude();
                                    String url ="https://maps.googleapis.com/maps/api/geocode/json?latlng="+String.valueOf(MyLat)+","+String.valueOf(MyLong)+"&key=[ENTER API KEY]";
                                    apiCall("weather",url);

                                }
                            }
                        }
                );
            } else {
                Toast.makeText(getActivity(), "Turn on location", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            requestPermissions();
        }
    }


    @SuppressLint("MissingPermission")
    private void requestNewLocationData(){

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(0);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        mFusedLocationClient.requestLocationUpdates(
                mLocationRequest, mLocationCallback,
                Looper.myLooper()
        );

    }

    private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            MyLat = mLastLocation.getLatitude();
            MyLong = mLastLocation.getLongitude();
            String url ="https://maps.googleapis.com/maps/api/geocode/json?latlng="+String.valueOf(MyLat)+","+String.valueOf(MyLong)+"&key=AIzaSyBF_Kyi8hraw4ez5lrPJZH3FbJPyPhOO6E";
            apiCall("weather",url);
        }
    };

    private boolean checkPermissions() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(
                getActivity(),
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSION_ID
        );
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            }
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        if (checkPermissions()) {
            //getLastLocation(); //decide if i need onResume
        }

    }

    public void apiCall(final String call, String url)
    {
        final TextView textView = (TextView) view.findViewById(R.id.text);
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            if(call.equals("weather"))
                            {
                                city = response.getJSONArray("results").getJSONObject(0).getJSONArray("address_components").getJSONObject(2).getString("long_name");
                                state = response.getJSONArray("results").getJSONObject(0).getJSONArray("address_components").getJSONObject(4).getString("long_name");
                                weatherCard(city);

                            }
                            else if(call.equals("weather_card"))
                            {
                                String summary = response.getJSONArray("weather").getJSONObject(0).getString("main");
                                String temp = String.valueOf(Integer.valueOf((int) (Double.valueOf(response.getJSONObject("main").getString("temp")) - 273.15)));
                                displayCard(summary, temp, city, state);
                            }
                        } catch (JSONException | UnsupportedEncodingException e) {
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
    public void weatherCard(String city) throws UnsupportedEncodingException {
        String url = "http://api.openweathermap.org/data/2.5/weather?q="+ URLEncoder.encode(city, "UTF-8")+"&appid=e1275589670730f5d3e807204945f592";
        apiCall("weather_card", url);
    }
    public void displayCard(String summary, String temp, String city, String state)
    {
        tempText.setText(temp + "\u00B0C");
        cityText.setText(city);
        stateText.setText(state);
        summaryText.setText(summary);
        if (summary.equals("Clouds"))
            imageView.setImageResource(R.drawable.clouds);
        else if (summary.equals("Clear"))
            imageView.setImageResource(R.drawable.clear);
        else if (summary.equals("Snow"))
            imageView.setImageResource(R.drawable.snow);
        else if (summary.equals("Rain") || summary.equals("Drizzle"))
            imageView.setImageResource(R.drawable.rain);
        else if (summary.equals("Thunderstorm"))
            imageView.setImageResource(R.drawable.thunderstorm);
        else
            imageView.setImageResource(R.drawable.default_img);






    }


}
