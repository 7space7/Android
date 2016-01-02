package com.ua.viktor.stormy.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ua.viktor.stormy.R;
import com.ua.viktor.stormy.ui.weather.Day;

/**
 * Created by viktor on 18.07.15.
 */
public class DayAdapter extends BaseAdapter {
    private Context mContext;
    private Day[]mDays;

    public  DayAdapter(Context context,Day[]days)
    {
        mContext=context;
        mDays=days;
    }

    @Override
    public int getCount() {
        return mDays.length;
    }

    @Override
    public Object getItem(int position) {
        return mDays[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
         if(convertView==null){
            convertView= LayoutInflater.from(mContext).inflate(R.layout.daily_item,null);
            holder=new ViewHolder();
            holder.iconImage=(ImageView)convertView.findViewById(R.id.iconImageView);
            holder.temperatureLabel=(TextView)convertView.findViewById(R.id.temperatureLabel);
            holder.dayLabel=(TextView)convertView.findViewById(R.id.dayNameLabel);
             convertView.setTag(holder);
        }else {
             holder=(ViewHolder)convertView.getTag();
         }
        Day day=mDays[position];
        holder.iconImage.setImageResource(day.getIconId());
        holder.temperatureLabel.setText(""+day.getTempratureMax());
        if(position==0) {
            holder.dayLabel.setText("Today");
        }else{
            holder.dayLabel.setText(day.getDayOfTheWeek() + "");
        }
        return convertView;
       }

    private static class ViewHolder{
        ImageView iconImage;
        TextView temperatureLabel;
        TextView dayLabel;
    }
}
