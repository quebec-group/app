package com.quebec.services;

import com.quebec.app.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Andy on 16/02/2017.
 */

public class UserFactory {

    private static String LOG_TAG = APIManager.class.getSimpleName();

    private static UserFactory instance;

    private UserFactory() {}

    public static UserFactory getInstance() {
        if (instance == null) {
            instance = new UserFactory();
        }

        return instance;
    }
    public User userFactory(JSONObject userJSON) throws JSONException {
        final String name = userJSON.getString("name");
        final String profileID = userJSON.getString("profileID");
        final String userID = userJSON.getString("userID");
        final String email = userJSON.getString("email");
        User user = new User(name);
        user.setEmail(email);
        user.setUserID(userID);
        user.setProfileID(profileID);
        return user;
    }



}
