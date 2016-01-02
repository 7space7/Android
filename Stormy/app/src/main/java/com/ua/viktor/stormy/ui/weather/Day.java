package com.ua.viktor.stormy.ui.weather;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by viktor on 17.07.15.
 */
public class Day implements Parcelable{
    private long mTime;
    private String mSummary;
    private double mTempratureMax;
    private String mIcon;
    private String mTimeZone;

    public long getTime() {
        return mTime;
    }
   public Day(){}

    public void setTime(long time) {
        mTime = time;
    }

    public String getSummary() {
        return mSummary;
    }

    public void setSummary(String summary) {
        mSummary = summary;
    }

    public double getTempratureMax() {
        mTempratureMax = ((mTempratureMax - 32)*5)/9;
        return Math.round(mTempratureMax);

    }

    public void setTempratureMax(double tempratureMax) {
        mTempratureMax = tempratureMax;
    }

    public String getIcon() {
        return mIcon;
    }

    public void setIcon(String icon) {
        mIcon = icon;
    }

    public String getTimeZone() {
        return mTimeZone;
    }
    public int getIconId(){
        return Forecast.getIconId(mIcon);
    }

    public String getDayOfTheWeek() {
        SimpleDateFormat formatter = new SimpleDateFormat("EEEE");
        formatter.setTimeZone(TimeZone.getTimeZone(mTimeZone));
        Date dateTime = new Date(mTime * 1000);
        return formatter.format(dateTime);
    }

    public void setTimeZone(String timeZone) {
        mTimeZone = timeZone;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(mTime);
        dest.writeString(mSummary);
        dest.writeDouble(mTempratureMax);
        dest.writeString(mIcon);
        dest.writeString(mTimeZone);
    }
   private Day(Parcel in){
    mTime=in.readLong();
    mSummary=in.readString();
    mTempratureMax=in.readDouble();
    mIcon=in.readString();
    mTimeZone=in.readString();
   }
    public static final Creator<Day>CREATOR=new Creator<Day>() {
        @Override
        public Day createFromParcel(Parcel source) {
            return new Day(source);
        }

        @Override
        public Day[] newArray(int size) {
            return new Day[size];
        }
    };
}
