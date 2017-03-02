package com.quebec.app;

import android.os.Parcel;
import android.os.Parcelable;

import com.amazonaws.mobile.content.ContentProgressListener;

/**
 * Created by Andrew on 04/02/2017.
 */

public class User implements Parcelable {

    private String name;
    private String email;
    private String userID;
    private String profileID;
    private boolean iFollow;

    public User(String name, String email, String userID, String profileID) {
        this.name = name;
        this.email = email;
        this.userID = userID;
        this.profileID = profileID;
    }

    public User(String name) {
        this.name = name;
    }

    public User() {
    }

    protected User(Parcel in) {
        name = in.readString();
        email = in.readString();
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

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getProfileID() {
        return profileID;
    }

    public void setProfileID(final String profileID) {
        this.profileID = profileID;
    }

    public String getUserID() {
        return userID;
    }

    public boolean doIFollow() {
        return iFollow;
    }

    public void setiFollow(boolean iFollow) {
        this.iFollow = iFollow;
    }

    public void getProfilePicture(ContentProgressListener callback) {
        if (profileID != null && !profileID.isEmpty()) {
            S3Handler.getInstance().getFile(getProfileID(), callback);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof User) {
            return ((User) obj).getUserID().equals(getUserID());
        }
        return super.equals(obj);
    }
}
