package com.ua.viktor.stormy.ui.weather;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by viktor on 16.07.15.
 */
public class CurrentWeather {
    private  String icon;
    private long time;
    private double mtemperature;
    private double mHumadity;
    private double mPreceipChance;
    private String mSummary;

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    private  String timeZone;
    public String getIcon() {
        return icon;
    }

    public int getIconId(){
      return Forecast.getIconId(icon);
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getmSummary() {
        return mSummary;
    }

    public void setmSummary(String mSummary) {
        this.mSummary = mSummary;
    }
public String getFormattedTime(){
    SimpleDateFormat format=new SimpleDateFormat("h:mm a");
    format.setTimeZone(TimeZone.getTimeZone(getTimeZone()));

    Date date=new Date(getTime()*1000);
    String time=format.format(date);
    return time;
}

    public double getmPreceipChance() {

        return mPreceipChance;
    }

    public void setmPreceipChance(double mPreceipChance) {
        this.mPreceipChance = mPreceipChance;
    }

    public double getmHumadity() {

        return mHumadity;
    }

    public void setmHumadity(double mHumadity) {
        this.mHumadity = mHumadity;
    }

    public long getTime() {

        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public double getMtemperature() {
        mtemperature = ((mtemperature - 32)*5)/9;
        return Math.round(mtemperature);
    }

    public void setMtemperature(double mtemperature) {
        this.mtemperature = mtemperature;
    }
}
