package com.quebec.app;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Andrew on 04/02/2017.
 */

public class User implements Parcelable {

    private String name;
    private String email;
    private String profileID;
    private String userID;

    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }

    protected User(Parcel in) {
        name = in.readString();
        email = in.readString();
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getProfileID() {
        return profileID;
    }

    public void setProfileID(String profileID) {
        this.profileID = profileID;
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
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
        parcel.writeString(email);
    }
}
