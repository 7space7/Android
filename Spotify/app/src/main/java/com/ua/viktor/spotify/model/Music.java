package com.ua.viktor.spotify.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by viktor on 21.07.15.
 */
public class Music implements Parcelable {
    private String mId;
    private String mName;
    private String mImgUrl;

    public Music() {

    }

    public String getImgUrl() {
        return mImgUrl;
    }

    public void setImgUrl(String imgUrl) {
        mImgUrl = imgUrl;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
dest.writeString(mId);
        dest.writeString(mName);
        dest.writeString(mImgUrl);
    }
    public Music(Parcel in){
        mId=in.readString();
        mName=in.readString();
        mImgUrl=in.readString();

    }
    public static final Creator<Music>CREATOR=new Creator<Music>() {
        @Override
        public Music createFromParcel(Parcel source) {
            return new Music(source);
        }

        @Override
        public Music[] newArray(int size) {
            return new Music[size];
        }
    };
}

