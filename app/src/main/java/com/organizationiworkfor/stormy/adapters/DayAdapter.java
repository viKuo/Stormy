package com.organizationiworkfor.stormy.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.organizationiworkfor.stormy.R;
import com.organizationiworkfor.stormy.weatherData.Day;


/**
 * Created by Vivien on 9/20/2015.
 */
public class DayAdapter extends BaseAdapter {
    private Day[] mDays;
    private Context mContext;

    public DayAdapter(Context context, Day[] days) {
        mDays = days;
        mContext = context;
    }

    @Override
    public int getCount() {
        return mDays.length;
    }

    @Override
    public Day getItem(int position) {
        return mDays[position];
    }

    @Override
    public long getItemId(int position) {
        return 0; //not using this
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = new ViewHolder();

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.daily_list_item, null);
            holder.iconImageView = (ImageView) convertView.findViewById(R.id.iconImageView);
            holder.hiTempValue = (TextView) convertView.findViewById(R.id.dailyHiTemp);
            holder.lowTempValue = (TextView) convertView.findViewById(R.id.dailyLowTemp);
            holder.dayLabel = (TextView) convertView.findViewById(R.id.dailyDayLabel);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Day day = mDays[position];
        if (position == 0) {
            holder.dayLabel.setText("Today");
        } else if (position == 1) {
            holder.dayLabel.setText("Tomorrow");
        } else {
            holder.dayLabel.setText(day.getDayOfTheWeek());
        }
        holder.iconImageView.setImageResource(day.getIconId());
        holder.hiTempValue.setText(day.getHiTemp() + "");
        holder.lowTempValue.setText(day.getLowTemp() + "");

        return convertView;
    }

    private static class ViewHolder {
        ImageView iconImageView;
        TextView hiTempValue;
        TextView lowTempValue;
        TextView dayLabel;
    }

}
