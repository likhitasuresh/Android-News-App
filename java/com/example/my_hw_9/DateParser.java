package com.example.my_hw_9;

import java.time.Clock;

import java.time.LocalDateTime;

public class DateParser {

    public static String parse (String type, String dateInString) {

        LocalDateTime dateTime = LocalDateTime.parse(dateInString.substring(0,19));
        LocalDateTime currentTime = LocalDateTime.now(Clock.systemUTC());
        if(type.equals("card"))
        {
            long secs = java.time.Duration.between(dateTime, currentTime).getSeconds();
            long mins = java.time.Duration.between(dateTime, currentTime).toMinutes();
            long hrs =  java.time.Duration.between(dateTime, currentTime).toHours();
            long days =  java.time.Duration.between(dateTime,currentTime).toDays();
            return GetTimeAgo(secs,mins,hrs,days);
        }
        else if(type.equals("bookmark"))
        {
            String month = dateTime.getMonth().toString().substring(0,1)+dateTime.getMonth().toString().substring(1,3).toLowerCase();
            return dateTime.getDayOfMonth() +" "+ month;
        }
        else
        {
            String month = dateTime.getMonth().toString().substring(0,1)+dateTime.getMonth().toString().substring(1,3).toLowerCase();
            return dateTime.getDayOfMonth() +" "+ month+ " "+ dateTime.getYear();
        }
    }
    static String GetTimeAgo(long secs, long mins, long hrs, long days)
    {
        if(secs < 60)
        {
            return secs + "s ago";
        }
        else if (mins < 60)
        {
            return mins + "m ago";
        }
        else if( hrs < 24)
        {
            return hrs + "h ago";
        }
        return days + "d ago";
    }
}

