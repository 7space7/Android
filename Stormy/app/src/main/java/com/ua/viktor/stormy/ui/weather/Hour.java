package com.ua.viktor.stormy.ui.weather;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by viktor on 17.07.15.
 */
public class Hour implements Parcelable {
    private long  mtime;
    private String mSummary;
    private  double mtemoerature;
    private String mIcon;
    private String mTimeZone;
   public Hour(){}
    public long getMtime() {

        return mtime;
    }

    public void setMtime(long mtime) {
        this.mtime = mtime;
    }

    public String getSummary() {
        return mSummary;
    }

    public void setSummary(String summary) {
        mSummary = summary;
    }

    public double getMtemoerature() {
        double temperature=((mtemoerature- 32)*5)/9;

        return Math.round(temperature);

    }
    public String getHour(){
        SimpleDateFormat format=new SimpleDateFormat("h a");
        format.setTimeZone(TimeZone.getTimeZone(getTimeZone()));
        Date date=new Date(getMtime()*1000);
        String time=format.format(date);
        return time;
    }
    public void setMtemoerature(double mtemoerature) {
        this.mtemoerature = mtemoerature;
    }
    public int getIconId(){
        return Forecast.getIconId(mIcon);
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

    public void setTimeZone(String timeZone) {
        mTimeZone = timeZone;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(mtime);
        dest.writeString(mSummary);
        dest.writeDouble(mtemoerature);
        dest.writeString(mIcon);
        dest.writeString(mTimeZone);
    }
    private Hour(Parcel in){
        mtime=in.readLong();
        mSummary=in.readString();
        mtemoerature=in.readDouble();
        mIcon=in.readString();
        mTimeZone=in.readString();
    }
    public static final Creator<Hour>CREATOR=new Creator<Hour>() {
        @Override
        public Hour createFromParcel(Parcel source) {
            return new Hour(source);
        }

        @Override
        public Hour[] newArray(int size) {
            return new Hour[size];
        }
    };
}
