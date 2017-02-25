package com.quebec.app;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Andrew on 25/02/2017.
 */
public class Video implements Parcelable {

    private String name;
    public Video(String name) {
        this.name = name;
    }

    protected Video(Parcel in) {
        name = in.readString();
    }

    public static final Parcelable.Creator<Video> CREATOR = new Parcelable.Creator<Video>() {
        @Override
        public Video createFromParcel(Parcel in) {
            return new Video(in);
        }

        @Override
        public Video[] newArray(int size) {
            return new Video[size];
        }
    };

    public String getName() {
        return this.name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
    }
}
