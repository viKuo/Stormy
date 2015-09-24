package com.organizationiworkfor.stormy.weatherData;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Vivien on 9/17/2015.
 */
public class Day implements Parcelable {
    private long mTime;
    private String mIcon;
    private String mSummary;
    private double mHiTemp;
    private double mLowTemp;
    private String mTimezone;
    private boolean isCelsius = true;

    public Day() { }

    public long getTime() {
        return mTime;
    }

    public void setTime(long time) {
        mTime = time;
    }

    public String getIcon() {
        return mIcon;
    }

    public void setIcon(String icon) {
        mIcon = icon;
    }

    public String getSummary() {
        return mSummary;
    }

    public void setSummary(String summary) {
        mSummary = summary;
    }

    public int getHiTemp() {
        if (isCelsius) {
            return (int) Math.round(mHiTemp - 32 * 5/9);
        } else {
            return (int) Math.round(mHiTemp);
        }
    }

    public void setHiTemp(double hiTemp) {
        mHiTemp = hiTemp;
    }

    public int getLowTemp() {
        if (isCelsius) {
            return (int) Math.round(mLowTemp - 32 * 5/9);
        } else {
            return (int) Math.round(mLowTemp);
        }
    }

    public void setLowTemp(double lowTemp) {
        mLowTemp = lowTemp;
    }

    public String getTimezone() {
        return mTimezone;
    }

    public void setTimezone(String timezone) {
        mTimezone = timezone;
    }

    public int getIconId() {
        return Forecast.getIconId(mIcon);
    }

    public String getDayOfTheWeek() {
        SimpleDateFormat formatter = new SimpleDateFormat("EEEE");
        formatter.setTimeZone(TimeZone.getTimeZone(mTimezone));
        Date dateTime = new Date(mTime * 1000);
        return formatter.format(dateTime);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(mHiTemp);
        dest.writeDouble(mLowTemp);
        dest.writeLong(mTime);
        dest.writeString(mIcon);
        dest.writeString(mSummary);
        dest.writeString(mTimezone);
    }

    private Day(Parcel in) {
        mHiTemp = in.readDouble();
        mLowTemp = in.readDouble();
        mTime = in.readLong();
        mIcon = in.readString();
        mSummary = in.readString();
        mTimezone = in.readString();

    }

    public static final Parcelable.Creator<Day> CREATOR = new Creator<Day>() {
        @Override
        public Day createFromParcel(Parcel source) {
            return new Day(source);
        }

        @Override
        public Day[] newArray(int size) {
            return new Day[size];
        }
    };
}
