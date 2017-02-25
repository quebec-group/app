package com.quebec.services;

import com.quebec.app.User;

import org.json.JSONException;

/**
 * Created by Andy on 16/02/2017.
 */

public class UserFactory {
    private UserDAO userDAO;

    public void setUserDAO (UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public User userFactory() throws JSONException {
        final String name = userDAO.get_DAO_BODY().getString("name");
        final String profileID = userDAO.get_DAO_BODY().getString("profileID");
        User user = new User(name, profileID);
        return user;
    }



}
