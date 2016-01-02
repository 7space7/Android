package com.ua.viktor.stormy.ui;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;

import com.ua.viktor.stormy.R;
import com.ua.viktor.stormy.ui.adapter.DayAdapter;
import com.ua.viktor.stormy.ui.weather.Day;

import java.util.Arrays;

public class DetailForecast extends ListActivity {

    private Day[] mDays;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_forecast);

        Intent intent=getIntent();
        Parcelable[]parceble=intent.getParcelableArrayExtra(MainActivity.DAILY_FORECAST);
        mDays= Arrays.copyOf(parceble,parceble.length,Day[].class);

        DayAdapter dayAdapter=new DayAdapter(this,mDays);
        setListAdapter(dayAdapter);
    }




}
