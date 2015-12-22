package com.ua.viktor.spotify.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by viktor on 01.09.15.
 */
public class Track implements Parcelable {
    private String mImageSmall;
    private String mImageBig;
    private String mPreviewUrl;
    private String mNameSong;

    public Track() {

    }

    public String getAlbum() {
        return mAlbum;
    }

    public void setAlbum(String album) {
        mAlbum = album;
    }

    private String mAlbum;

    public String getNameSong() {
        return mNameSong;
    }

    public void setNameSong(String nameSong) {
        mNameSong = nameSong;
    }

    public String getImageBig() {
        return mImageBig;
    }

    public void setImageBig(String imageBig) {
        mImageBig = imageBig;
    }

    public String getPreviewUrl() {
        return mPreviewUrl;
    }

    public void setPreviewUrl(String previewUrl) {
        mPreviewUrl = previewUrl;
    }


    public void setImageSmall(String imageSmall) {
        mImageSmall = imageSmall;
    }

    public String getImageSmall() {
        return mImageSmall;
    }

    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mAlbum);
        dest.writeString(mImageSmall);
        dest.writeString(mNameSong);
        dest.writeString(mImageBig);
        dest.writeString(mPreviewUrl);

    }
    public Track(Parcel in){
        mImageBig=in.readString();
        mNameSong=in.readString();
        mAlbum=in.readString();
        mPreviewUrl=in.readString();
        mImageSmall=in.readString();
    }
    public static final Creator<Track>CREATOR=new Creator<Track>() {
        @Override
        public Track createFromParcel(Parcel source) {
            return new Track(source);
        }

        @Override
        public Track[] newArray(int size) {
            return new Track[size];
        }
    };
}
