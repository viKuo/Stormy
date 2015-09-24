package com.organizationiworkfor.stormy.UI;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.organizationiworkfor.stormy.R;
import com.organizationiworkfor.stormy.weatherData.Current;
import com.organizationiworkfor.stormy.weatherData.Day;
import com.organizationiworkfor.stormy.weatherData.Forecast;
import com.organizationiworkfor.stormy.weatherData.Hour;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = MainActivity.class.getSimpleName().toString();
    public static final String DAILY_FORECAST = "DAILY_FORECAST_ACTIVITY";
    public static final String HOURLY_FORECAST = "HOURLY_FORECAST_ACTIVITY";
    private Forecast mForecast = new Forecast();

    @Bind (R.id.apparentValue) TextView mApparentValue;
    @Bind (R.id.hiTempValue) TextView mHiTempValue;
    @Bind (R.id.locationTextView) TextView mLocationTextView;
    @Bind (R.id.lowTempValue) TextView mLowTempValue;
    @Bind (R.id.precipValue) TextView mPrecipValue;
    @Bind (R.id.summaryTextView) TextView mSummaryTextView;
    @Bind (R.id.timeTextView) TextView mTimeTextView;
    @Bind (R.id.tempValue) TextView mTempValue;
    @Bind (R.id.iconImageView) ImageView mIconImageView;
    @Bind (R.id.progressBar) ProgressBar mProgressBar;
    @Bind (R.id.refreshImageView) ImageView mRefreshImageView;
    double latitude = 37.8267;
    double longitude = -122.423;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        getForecast();
        refresh();
        Log.d(TAG, "Main UI code is running");
    }

    @OnClick(R.id.refreshImageView)
    public void refresh(){
        getForecast();
    }

    @OnClick(R.id.dailyButton)
    public void getDailyForecast() {
        Intent intent = new Intent(this, DailyForecastActivity.class);
        intent.putExtra(DAILY_FORECAST, mForecast.getDaily());
        startActivity(intent);
    }

    @OnClick(R.id.hourlyButton)
    public void getHourlyForecast(){
        Intent intent = new Intent (this, HourlyForecastActivity.class);
        intent.putExtra(HOURLY_FORECAST, mForecast.getHourly());
        startActivity(intent);
    }

    private void toggleVisibility() {
        if (mProgressBar.getVisibility() == View.INVISIBLE) {
            mProgressBar.setVisibility(View.VISIBLE);
            mRefreshImageView.setVisibility(View.INVISIBLE);
        } else {
            mProgressBar.setVisibility(View.INVISIBLE);
            mRefreshImageView.setVisibility(View.VISIBLE);
        }
    }

    private void getForecast() {
        String apiKey = "dfa12989842c02793a353f90a5104fe1";
        String forecastURL = "https://api.forecast.io/forecast/"
                + apiKey + "/" + latitude + "," + longitude;

        if (isNetworkAvailable()) {
            toggleVisibility();
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(forecastURL)
                    .build();

            Call call = client.newCall(request);
            //background working thread
            call.enqueue(new Callback() {
                             @Override
                             public void onFailure(Request request, IOException e) {
                             }

                             @Override
                             public void onResponse(Response response) throws IOException {
                                 String jsonData = response.body().string();
                                 Log.v(TAG, jsonData);
                                 try {
                                     if (response.isSuccessful()) {
                                         parseForecastData(jsonData);
                                         //brings background worker thread to main thread
                                         runOnUiThread(new Runnable() {
                                             @Override
                                             public void run() {
                                                 toggleVisibility();
                                                 updateDisplay();
                                             }
                                         });
                                     } else {
                                         alertUserAboutError();
                                     }
                                 } catch (JSONException e) {
                                     Log.e(TAG, "Exception caught", e);
                                 }
                             }
                         }
            );
        } else {
            Toast.makeText(this, R.string.network_unavailable, Toast.LENGTH_LONG).show();
        }
    }

    private Forecast parseForecastData(String jsonData) throws JSONException {
        mForecast.setCurrently(getCurrentWeather(jsonData));
        mForecast.setDaily(getDailyWeather(jsonData));
        mForecast.setHourly(getHourlyWeather(jsonData));
        return mForecast;
    }

    private Hour[] getHourlyWeather(String jsonData) throws JSONException {
        JSONObject forecast = new JSONObject(jsonData);
        String timezone = forecast.getString("timezone");
        JSONObject hourly = forecast.getJSONObject("hourly");
        JSONArray hourlyDataArray = hourly.getJSONArray("data");
        Hour[] hourlyData = new Hour[hourlyDataArray.length()];

        for (int i = 0; i < hourlyDataArray.length(); i++) {
            JSONObject hourlyObject = hourlyDataArray.getJSONObject(i);
            hourlyData[i] = new Hour();
            hourlyData[i].setIcon(hourlyObject.getString("icon"));
            hourlyData[i].setSummary(hourlyObject.getString("summary"));
            hourlyData[i].setTemperature(hourlyObject.getDouble("temperature"));
            hourlyData[i].setTime(hourlyObject.getLong("time"));
            hourlyData[i].setTimezone(timezone);
        }
        return hourlyData;
    }

    private Day[] getDailyWeather(String jsonData) throws JSONException {
        JSONObject forecast = new JSONObject(jsonData);
        String timezone = forecast.getString("timezone");
        JSONObject daily = forecast.getJSONObject("daily");
        JSONArray dailyDataArray = daily.getJSONArray("data");
        Day[] dailyData = new Day[dailyDataArray.length()];

        for (int i = 0; i < dailyDataArray.length(); i++) {
            JSONObject dailyObject = dailyDataArray.getJSONObject(i);
            dailyData[i] = new Day();
            dailyData[i].setIcon(dailyObject.getString("icon"));
            dailyData[i].setSummary(dailyObject.getString("summary"));
            dailyData[i].setHiTemp(dailyObject.getDouble("temperatureMax"));
            dailyData[i].setLowTemp(dailyObject.getDouble("temperatureMin"));
            dailyData[i].setTime(dailyObject.getLong("time"));
            dailyData[i].setTimezone(timezone);
        }
       return dailyData;
    }

    private Current getCurrentWeather(String jsonData) throws JSONException {
        JSONObject forecast = new JSONObject(jsonData);
        String timezone = forecast.getString("timezone");
        Log.i(TAG, timezone);

        //current data
        JSONObject currently = new JSONObject(jsonData).getJSONObject("currently");
        Current current = new Current();
        current.setApparentTemp(currently.getDouble("apparentTemperature"));
        current.setPrecipChance(currently.getDouble("precipProbability"));
        current.setTemperature(currently.getDouble("temperature"));
        current.setTime(currently.getLong("time"));
        current.setIcon(currently.getString("icon"));
        current.setSummary(currently.getString("summary"));
        current.setTimeZone(timezone);

        JSONArray tempDifference = new JSONObject(jsonData).getJSONObject("hourly").getJSONArray("data");
        current.setTempRange(tempDifference);

        Log.i(TAG, current.getFormattedTime());
        return current;
    }

    private void updateDisplay() {
        Current current = mForecast.getCurrently();
        mApparentValue.setText(current.getApparentTemp() + "\u00B0" + "");
        mHiTempValue.setText(current.getHiTemp() + "\u00B0" + "");
        mLowTempValue.setText(current.getLowTemp() + "\u00B0" + "");
        mPrecipValue.setText(current.getPrecipChance() + "");
        mSummaryTextView.setText(current.getSummary());
        mTempValue.setText(current.getTemperature() + "");
        mTimeTextView.setText("At " + current.getFormattedTime() + " it will be");
        Drawable drawable;
        //stupid depreciated class urgh
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            drawable = getResources().getDrawable(current.getIconId(), getTheme());
        } else {
            drawable = getResources().getDrawable(current.getIconId());
        }
        mIconImageView.setImageDrawable(drawable);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            isAvailable = true;
        }
        return isAvailable;
    }


    private void alertUserAboutError() {
        AlertDialogFragment dialog = new AlertDialogFragment();
        dialog.show(getFragmentManager(), "Error Dialog");
    }

}
