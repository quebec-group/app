package com.quebec.services;

import android.util.Log;

import com.quebec.app.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Andy on 23/02/2017.
 * FriendListFactory: return an ArrayList<User> of friends
 */

public class FriendListFactory {
    private BaseDAO baseDAO;
    private static String LOG_TAG =FriendListFactory.class.getSimpleName();

    public void setFriendListDAO (BaseDAO baseDAO) {
        this.baseDAO = baseDAO;
    }

    public ArrayList<User> friendListFactory() throws JSONException {
        JSONObject jsonObject = baseDAO.get_DAO_BODY();


        String s = jsonObject.getString("friends");
        JSONArray jsonArray = new JSONArray(s);
        User user;
        ArrayList<User> friends = new ArrayList<>();

        // for all users in the jsonObject
        for(int i=0;i<jsonArray.length();i++) {
            // create a user object from JSON
            JSONObject currentUser = new JSONObject(jsonArray.getString(i));
            Log.d(LOG_TAG, currentUser.toString());
            final String profileID = currentUser.getString("profileID");
            final String name = currentUser.getString("name");
            final String email = currentUser.getString("email");
            final String userID = currentUser.getString("userID");
            user = new User(name, profileID);
            user.setEmail(email);
            user.setUserID(userID);
            friends.add(user);
        }
        return friends;
    }
}

