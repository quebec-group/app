package com.quebec.services;

/**
 * Created by Andy on 14/02/2017.
 */

public class UserDTO {
    private String userID;
    private String profileID;
    private String name;
    private String email;

    public UserDTO(String userID, String profileID, String name, String email) {
        this.userID = userID;
        this.profileID = profileID;
        this.name = name;
        this.email = email;
    }

}
