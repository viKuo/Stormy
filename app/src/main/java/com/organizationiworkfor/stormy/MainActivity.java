package com.organizationiworkfor.stormy;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
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
    String TAG = MainActivity.class.getSimpleName().toString();
    private CurrentWeather mCurrentWeather = new CurrentWeather();

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
                             public void onFailure(Request request, IOException e) {  }
                             @Override
                             public void onResponse(Response response) throws IOException {
                                 String jsonData = response.body().string();
                                 try {
                                     if (response.isSuccessful()) {
                                         mCurrentWeather = getWeatherDetails(jsonData);
                                         //brings background worker thread to main thread
                                         Log.d("finished getting data", mCurrentWeather.getTemperature() +"");
                                         runOnUiThread(new Runnable() {
                                             @Override
                                             public void run() {
                                                 toggleVisibility();
                                                 updateDisplay();
                                                 Log.i("update display", mCurrentWeather.getTemperature() + "");
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

    private void updateDisplay() {
        mApparentValue.setText(mCurrentWeather.getApparentTemp() + "\u00B0" + "");
        mHiTempValue.setText(mCurrentWeather.getHiTemp() + "\u00B0" + "");
        mLowTempValue.setText(mCurrentWeather.getLowTemp() + "\u00B0" + "");
        mPrecipValue.setText(mCurrentWeather.getPrecipChance() + "");
        mSummaryTextView.setText(mCurrentWeather.getSummary());
        mTempValue.setText(mCurrentWeather.getTemperature() + "");
        mTimeTextView.setText("At " + mCurrentWeather.getFormattedTime() + " it will be");
        Drawable drawable;
        //stupid depreciated class urgh
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            drawable = getResources().getDrawable(mCurrentWeather.getIconId(), getTheme());
        } else {
            drawable = getResources().getDrawable(mCurrentWeather.getIconId());
        }
        mIconImageView.setImageDrawable(drawable);
    }

    private CurrentWeather getWeatherDetails(String jsonData) throws JSONException {
        JSONObject forecast = new JSONObject(jsonData);
        String timezone = forecast.getString("timezone");
        Log.i(TAG, timezone);

        //current data
        JSONObject currently = new JSONObject(jsonData).getJSONObject("currently");
        CurrentWeather currentWeather = new CurrentWeather();
        currentWeather.setApparentTemp(currently.getDouble("apparentTemperature"));
        currentWeather.setPrecipChance(currently.getDouble("precipProbability"));
        currentWeather.setTemperature(currently.getDouble("temperature"));
        currentWeather.setTime(currently.getLong("time"));
        currentWeather.setIcon(currently.getString("icon"));
        currentWeather.setSummary(currently.getString("summary"));
        currentWeather.setTimeZone(timezone);

        JSONArray tempDifference = new JSONObject(jsonData).getJSONObject("hourly").getJSONArray("data");
        currentWeather.setTempRange(tempDifference);

        Log.i(TAG, currentWeather.getFormattedTime());
        return currentWeather;
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
