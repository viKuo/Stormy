package com.organizationiworkfor.stormy.UI;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.organizationiworkfor.stormy.R;
import com.organizationiworkfor.stormy.adapters.DayAdapter;
import com.organizationiworkfor.stormy.weatherData.Day;

import java.lang.reflect.Array;
import java.util.Arrays;

public class DailyForecastActivity extends ListActivity {
    private Day[] mDays;
    //if regular activity:
    //@Bind(R.id.listView) ListView mListView
    //@Bind (R.id.emptyView) TextView mEmptyView

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_forecast);
        //bind butterknife here

        /* this code is for a generic list adapter

        String[] daysOfWeek = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                daysOfWeek);
        setListAdapter(adapter); */

        Intent intent = getIntent();
        Parcelable[] parcelables = intent.getParcelableArrayExtra(MainActivity.DAILY_FORECAST);
        mDays = Arrays.copyOf(parcelables, parcelables.length, Day[].class);

        DayAdapter adapter = new DayAdapter(this, mDays);
        setListAdapter(adapter);
        //this line would be:
        //mListView.setAdapter(adapter);
        //mListView.setEmptyView(mEmptyView);
        //mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() { ...
        //^ creates another anonymous method and code what you want in here


    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        //you can use the item passed into the adapter, such as mDays array above,
        // where position is the position in the array;
        //this is only for listActivities

        Toast.makeText(this, "you clicked something!", Toast.LENGTH_LONG).show();
    }
}
