package com.organizationiworkfor.stormy.weatherData;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Vivien on 9/17/2015.
 */
public class Hour implements Parcelable {
    private long mTime;
    private String mSummary;
    private double mTemperature;
    private String mIcon;
    private String mTimezone;
    private boolean isCelsius = true;

    public Hour() { }

    public long getTime() {
        return mTime;
    }

    public void setTime(long time) {
        mTime = time;
    }

    public String getSummary() {
        return mSummary;
    }

    public void setSummary(String summary) {
        mSummary = summary;
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

    public String getIcon() {
        return mIcon;
    }

    public int getIconId() {
        return Forecast.getIconId(mIcon);
    }

    public void setIcon(String icon) {
        mIcon = icon;
    }

    public String getTimezone() {
        return mTimezone;
    }

    public void setTimezone(String timezone) {
        mTimezone = timezone;
    }

    @Override
    public int describeContents() {
        return 0; // ignore
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(mTemperature);
        dest.writeLong(mTime);
        dest.writeString(mTimezone);
        dest.writeString(mIcon);
        dest.writeString(mSummary);
    }

    private Hour(Parcel parcel) {
        mTemperature = parcel.readDouble();
        mTime = parcel.readLong();
        mTimezone = parcel.readString();
        mIcon = parcel.readString();
        mSummary = parcel.readString();
    }

    public static Creator<Hour> CREATOR = new Creator<Hour>() {
        @Override
        public Hour createFromParcel(Parcel source) {
            return new Hour(source);
        }

        @Override
        public Hour[] newArray(int size) {
            return new Hour[size];
        }
    };

    public String getHour() {
        SimpleDateFormat formatter = new SimpleDateFormat("h a");
        formatter.setTimeZone(TimeZone.getTimeZone(mTimezone));
        Date hourTime = new Date(mTime *1000);
        return formatter.format(hourTime);
    }
}
