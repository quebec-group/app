package com.quebec.app;

import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.quebec.services.APICallback;
import com.quebec.services.APIManager;

import java.util.List;

/**
 * Created by callum on 01/03/2017.
 */

public class RelatedUsersListFragment extends FriendsListFragment {
    private static String LOG_TAG = RelatedUsersListFragment.class.getSimpleName();
    private boolean followsMe;

    public static RelatedUsersListFragment newInstance(boolean followsMe) {
        RelatedUsersListFragment fragment = new RelatedUsersListFragment();
        fragment.followsMe = followsMe;
        return fragment;
    }

    @Override
    protected String getTitle() {
        if (followsMe) {
            return "Followers";
        } else {
            return "Following";
        }
    }

    @Override
    public void setData() {
        APICallback<List<User>> callback = new APICallback<List<User>>() {
            @Override
            public void onSuccess(List<User> users) {
                setUsers(users);
            }

            @Override
            public void onFailure(String message) {
                Log.d(LOG_TAG, "Couldn't get followers: " + message);
            }
        };

        if (followsMe) {
            APIManager.getInstance().followers(callback);
        } else {
            APIManager.getInstance().following(callback);
        }
    }

    @Override
    public void setupButton(final Button button, int positon) {
        button.setText("Unfollow");
        final User user = getUsers().get(positon);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                // Give user ability to follow/unfollow user dependent on current state
                Log.d(LOG_TAG, button.getText().toString());
                if("Follow".equals(button.getText().toString())) {
                    APIManager.getInstance().follow(user, new APICallback<String>() {
                        @Override
                        public void onSuccess(String responseBody) {
                            button.setText("Unfollow");
                        }

                        @Override
                        public void onFailure(String message) {
                            Log.d(LOG_TAG, "Failed to follow: " + user.getUserID());
                        }
                    });
                } else {
                    APIManager.getInstance().unfollow(user, new APICallback<String>() {
                        @Override
                        public void onSuccess(String responseBody) {
                            button.setText("Follow");
                        }

                        @Override
                        public void onFailure(String message) {
                            Log.d(LOG_TAG, "Failed to unfollow: " + user.getUserID());
                        }
                    });
                }

            }
        });
    }
}
