package com.organizationiworkfor.stormy.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.organizationiworkfor.stormy.R;
import com.organizationiworkfor.stormy.weatherData.Hour;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Vivien on 9/24/2015.
 */
public class HourAdapter extends RecyclerView.Adapter<HourAdapter.HourViewHolder> {
    private Hour[] mHours;
    private Context mContext;

    public HourAdapter(Context context, Hour[] hours) {
        mContext = context;
        mHours = hours;
    }

    @Override
    public HourViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.hourly_list_item, null);
        //on that null:  If root was supplied, this is the root View; otherwise it is the root of the inflated XML file.
        HourViewHolder holder = new HourViewHolder (view);
        return holder;
    }

    @Override
    public void onBindViewHolder(HourViewHolder holder, int position) {
        holder.bindHour(mHours[position]);
    }

    @Override
    public int getItemCount() {
        return mHours.length;
    }

    public class HourViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @Bind(R.id.iconImageView) ImageView mHourlyIcon;
        @Bind(R.id.hourlySummary) TextView mHourlySummary;
        @Bind(R.id.hourlyTemp) TextView mHourlyTemp;
        @Bind(R.id.hourlyTime) TextView mhourlyTime;

        public HourViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            //set onClickListener to each list item view
            //HourViewHolder implements View.OnClickListner, hence the parameter is this
            itemView.setOnClickListener(this);
        }

        public void bindHour(Hour hour) {
            mHourlyIcon.setImageResource(hour.getIconId());
            mHourlySummary.setText(hour.getSummary());
            mHourlyTemp.setText(hour.getTemperature() + "");
            mhourlyTime.setText(hour.getHour());
        }

        //this is the way to implement an onclick listener for recycler views
        @Override
        public void onClick(View v) {
            Toast.makeText(mContext, "you click something!", Toast.LENGTH_LONG).show();
        }
    }

}
