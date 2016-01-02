package com.ua.viktor.stormy.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.ua.viktor.stormy.R;
import com.ua.viktor.stormy.ui.adapter.HourAdapter;
import com.ua.viktor.stormy.ui.weather.Hour;

import java.util.Arrays;

public class HourlyForecastActivity extends ActionBarActivity {
    private Hour[]mHours;
    private RecyclerView mRecyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hourly_forecast);
        mRecyclerView=(RecyclerView)findViewById(R.id.rv);

        Intent intent=getIntent();
        Parcelable[]parcelables=intent.getParcelableArrayExtra(MainActivity.HORLY_FORECAST);
        mHours= Arrays.copyOf(parcelables,parcelables.length,Hour[].class);




        HourAdapter hourAdapter=new HourAdapter(mHours);
        mRecyclerView.setAdapter(hourAdapter);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setHasFixedSize(true);
    }


}
