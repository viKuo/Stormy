package com.organizationiworkfor.stormy.weatherData;

import com.organizationiworkfor.stormy.R;

/**
 * Created by Vivien on 9/17/2015.
 */
public class Forecast {
    private Current mCurrently;
    private Hour[] mHourly;
    private Day[] mDaily;

    public Current getCurrently() {
        return mCurrently;
    }

    public void setCurrently(Current currently) {
        mCurrently = currently;
    }

    public Hour[] getHourly() {
        return mHourly;
    }

    public void setHourly(Hour[] hourly) {
        mHourly = hourly;
    }

    public Day[] getDaily() {
        return mDaily;
    }

    public void setDaily(Day[] daily) {
        mDaily = daily;
    }

    public static int getIconId(String iconString) {
        int iconId = R.drawable.clear_day;
        if (iconString.equals("clear-day")) {
            iconId = R.drawable.clear_day;
        } else if (iconString.equals("clear-night")) {
            iconId = R.drawable.clear_night;
        } else if (iconString.equals("rain")) {
            iconId = R.drawable.rain;
        } else if (iconString.equals("snow")) {
            iconId = R.drawable.snow;
        } else if (iconString.equals("sleet")) {
            iconId = R.drawable.sleet;
        } else if (iconString.equals("wind")) {
            iconId = R.drawable.wind;
        } else if (iconString.equals("fog")) {
            iconId = R.drawable.fog;
        } else if (iconString.equals("cloudy")) {
            iconId = R.drawable.cloudy;
        } else if (iconString.equals("partly-cloudy-day")) {
            iconId = R.drawable.partly_cloudy;
        } else if (iconString.equals("partly-cloudy-night")) {
            iconId = R.drawable.cloudy_night;
        }
        return iconId;
    }
}
