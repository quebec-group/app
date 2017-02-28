package com.quebec.services;

import android.util.Log;

import com.quebec.app.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Andy on 23/02/2017.
 * UserListFactory: return an ArrayList<User> of friends
 */

public class UserListFactory {

    private static String LOG_TAG =UserListFactory.class.getSimpleName();


    public ArrayList<User> userListFactory(JSONArray jsonArray) throws JSONException {
        User user;
        ArrayList<User> friends = new ArrayList<>();

        // for all users in the jsonObject
        for(int i=0;i<jsonArray.length();i++) {
            // create a user object from JSON
            JSONObject currentUser = new JSONObject(jsonArray.getString(i));
            final String profileID = currentUser.getString("profileID");
            final String name = currentUser.getString("name");
            final String email = currentUser.getString("email");
            final String userID = currentUser.getString("userID");
            user = new User(name);
            user.setEmail(email);
            user.setUserID(userID);
            user.setProfileID(profileID);
            friends.add(user);
        }
        return friends;
    }
}

