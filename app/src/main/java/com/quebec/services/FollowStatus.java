package com.quebec.services;

import android.util.Log;

import com.quebec.app.MainActivity;
import com.quebec.app.ProfileFriendFragment;
import com.quebec.app.User;

/**
 * Created by Andy on 28/02/2017.
 */

public class FollowStatus {
    private static FollowStatus instance;
    private static String LOG_TAG = FollowStatus.class.getSimpleName();

    private FollowStatus() {}

    public static FollowStatus getInstance() {
        if (instance == null) {
            instance = new FollowStatus();
        }

        return instance;
    }


}
