package com.example.my_hw_9;

import android.util.Log;

public class NewsCard {

    private String headline, time, source;
    private String image, id, webUrl;




    public NewsCard(String headline, String time, String source, String image, String id, String webUrl) {
        this.headline = headline;
        this.time = time;
        this.source = source;
        this.image = image;
        this.id = id;
        this.webUrl = webUrl;

    }

    public String getHeadline() {
        return headline;
    }

    public String getTime() {
        return time;
    }

    public String getSource() {
        return source;
    }

    public String getId() {
        return id;
    }

    public String getImage() {
        return image;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public void openFragment()
    {
        Log.i("yes", "ok");
    }




}
