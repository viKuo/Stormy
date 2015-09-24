package com.organizationiworkfor.stormy.weatherData;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Vivien on 9/16/2015.
 */
public class Current {
    private String mIcon;
    private long mTime;
    private double mTemperature;
    private String mSummary;
    private double mPrecipChance;
    private double mApparentTemp;
    private String mTimeZone;
    private double mHiTemp = -255;
    private double mLowTemp = 255;
    private boolean isCelsius = true;

    public String getFormattedTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("h:mm a");
        //needs to amke sure timezone is already set first
        formatter.setTimeZone(TimeZone.getTimeZone(getTimeZone()));
        Date dateTime = new Date(getTime() * 1000);
        String timeString = formatter.format(dateTime);
        return timeString;
    }

    public int getIconId(){
        return Forecast.getIconId(mIcon);
    }

    public String getIcon() {
        return mIcon;
    }

    public void setIcon(String icon) {
        mIcon = icon;
    }

    public long getTime() {
        return mTime;
    }

    public void setTime(long time) {
        mTime = time;
    }

    public int getTemperature() {
        if (isCelsius) {
            return (int) Math.round(mTemperature - 32 * 5/9);
        }
        return (int) Math.round(mTemperature);
    }

    public void setTemperature(double temperature) {
        mTemperature = temperature;
    }

    public String getSummary() {
        return mSummary;
    }

    public void setSummary(String summary) {
        mSummary = summary;
    }

    public String getPrecipChance() {
        String chance = Math.round(mPrecipChance * 100) +"%";
        return chance;
    }

    public void setPrecipChance(double precipChance) {
        mPrecipChance = precipChance;
    }

    public int getApparentTemp() {
        if (isCelsius) {
            return (int) Math.round(mApparentTemp - 32 * 5/9);
        }
        return (int) Math.round(mApparentTemp);
    }

    public void setApparentTemp(double apparentTemp) {
        mApparentTemp = apparentTemp;
    }

    public String getTimeZone() {
        return mTimeZone;
    }

    public void setTimeZone(String timeZone) {
        mTimeZone = timeZone;
    }

    public void setCelsius(boolean celsius) {
        this.isCelsius = celsius;
    }

    public void setTempRange(JSONArray tempDifference) {
        DateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        if (mTimeZone == null) {
            Log.i("timezone", "timezone problems.");
        }else {
            //getting current date into a time string
            formatter.setTimeZone(TimeZone.getTimeZone(mTimeZone));
            Date dateTime = new Date(getTime() * 1000);
            String timeString = formatter.format(dateTime);
            Log.d("timestring", timeString);
            long midnightUnixTime = 0;
            try {
                //turning current time into unix time
                midnightUnixTime = formatter.parse(timeString).getTime();
                midnightUnixTime = (midnightUnixTime / 1000) + (21*60*60) + (59*60) + 59; //midnightUnixTime set to midnight
            } catch (ParseException e) {
                Log.d("parsing", "parse exception", e);
            }
            Log.i("unix timestamp", midnightUnixTime + "");

            //walk through array
            for (int i = 0; i <= 12; i++) {
                Log.i("for loop", i + "");
                try {
                    JSONObject hourlyData = tempDifference.getJSONObject(i);
                    long hourlyTime = hourlyData.getLong("time");
                    double hourlyTemp = hourlyData.getDouble("temperature");
                    if (hourlyTime < midnightUnixTime){
                        //hourly data is still of today
                        if (hourlyTemp > mHiTemp) {
                            mHiTemp = hourlyTemp;
                            Log.i("hi temp", mHiTemp + "");
                        }
                        if (hourlyTemp < mLowTemp) {
                            mLowTemp = hourlyTemp;
                            Log.i("low temp", mLowTemp + "");
                        }
                    } else {
                        //the data parsing is already passed today's date
                        Log.i("breaking", "breaking out of for");
                        break;
                    }
                } catch (JSONException e) {
                    Log.e("currentWeather", "JSON exception", e);
                }

            }

        }

    }

    public int getHiTemp() {
        if (isCelsius) {
            return (int) Math.round(mHiTemp - 32 * 5/9);
        }
        return (int) Math.round(mHiTemp);
    }

    public int getLowTemp() {
        if (isCelsius) {
            return (int) Math.round(mLowTemp - 32 * 5/9);
        }
        return (int) Math.round(mLowTemp);
    }

}
