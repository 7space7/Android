package com.ua.viktor.movies.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by viktor on 19.07.15.
 */
public class Movie implements Parcelable {
    private String mTitle;
    private String mOverview;
    private String mDate;
    private double mRating;
    private String mImageUrl;

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    private int mId;
    public String getDate() {
        return mDate;
    }
    public Movie(){
    }
    public void setDate(String date) {
        mDate = date;
    }

    public double getRating() {
        return mRating;
    }

    public void setRating(double rating) {
        mRating = rating;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        mImageUrl = imageUrl;
    }

    public String getOverview() {
        return mOverview;
    }

    public void setOverview(String overview) {
        mOverview = overview;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mTitle);
        dest.writeString(mOverview);
        dest.writeString(mDate);
        dest.writeDouble(mRating);
        dest.writeString(mImageUrl);
        dest.writeInt(mId);
    }
    private Movie(Parcel in){
        mTitle=in.readString();
        mOverview=in.readString();
        mDate=in.readString();
        mRating=in.readDouble();
        mImageUrl=in.readString();
        mId=in.readInt();
    }
    public static final Creator<Movie>CREATOR=new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
