package com.ua.viktor.stormy.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ua.viktor.stormy.R;
import com.ua.viktor.stormy.ui.weather.Hour;

/**
 * Created by viktor on 19.07.15.
 */
public class HourAdapter extends RecyclerView.Adapter<HourAdapter.HourViewHolder> {
    private Hour[]mHours;
    public HourAdapter(Hour[] hours){
        mHours=hours;
    }

    @Override
    public HourViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.hourly_list_item, parent, false);
        HourViewHolder hourViewHolder=new HourViewHolder(view);
        return hourViewHolder;
    }

    @Override
    public void onBindViewHolder(HourViewHolder holder, int position) {
     holder.bindHour(mHours[position]);
    }

    @Override
    public int getItemCount() {
        return mHours.length;
    }

    public class HourViewHolder extends RecyclerView.ViewHolder {
        public TextView mTimeLabel;
        public TextView mSummary;
        public TextView mTemperature;
        public ImageView mIcon;

        public HourViewHolder(View itemView) {
            super(itemView);
            mTimeLabel = (TextView) itemView.findViewById(R.id.timeTextView1);
            mSummary = (TextView) itemView.findViewById(R.id.summaryView);
            mTemperature = (TextView) itemView.findViewById(R.id.temperatureLabel1);
            mIcon = (ImageView) itemView.findViewById(R.id.iconView);
        }

        public void bindHour(Hour hour) {
           mTimeLabel.setText(hour.getHour());
            mSummary.setText(hour.getSummary());
            mTemperature.setText(hour.getMtemoerature()+"");
            mIcon.setImageResource(hour.getIconId());
        }
    }
}
