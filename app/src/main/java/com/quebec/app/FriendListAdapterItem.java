package com.quebec.app;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.quebec.services.APICallback;
import com.quebec.services.APIManager;
import com.quebec.services.FollowStatus;
import com.quebec.services.FollowStatusCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Andrew on 03/02/2017.
 */


public class FriendListAdapterItem extends ArrayAdapter<User>{

    int layoutResourceID;
    List<User> data = new ArrayList<>();
    List<Boolean> togg = new ArrayList<>();
    Context mContext;

    private static String LOG_TAG = FriendListAdapterItem.class.getSimpleName();



    public FriendListAdapterItem(Context mContext, int layoutResourceID, List<User> objects) {
        super(mContext, layoutResourceID, objects);

        this.layoutResourceID = layoutResourceID;
        this.mContext = mContext;
        this.data = objects;

    }

    public FriendListAdapterItem(Context mContext, int layoutResourceID) {
        this(mContext, layoutResourceID, new ArrayList<User>());
    }

    /**
     * Populates the item in the list.
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(layoutResourceID, parent, false);
        }

        final User user = data.get(position);

        TextView textViewItem = (TextView) convertView.findViewById(R.id.friendItemName);
        textViewItem.setText(user.getName());

        final Button followToggleButton = (Button) convertView.findViewById(R.id.followToggleButton);
        followToggleButton.setText("Unfollow");


        followToggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                // Give user ability to follow/unfollow user dependent on current state
                Log.d(LOG_TAG, followToggleButton.getText().toString());
                if(followToggleButton.getText().toString() == "Follow") {
                    APIManager.getInstance().follow(user, new APICallback<String>() {
                        @Override
                        public void onSuccess(String responseBody) {
                            followToggleButton.setText("Unfollow");
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
                            followToggleButton.setText("Follow");
                        }

                        @Override
                        public void onFailure(String message) {
                            Log.d(LOG_TAG, "Failed to unfollow: " + user.getUserID());
                        }
                    });
                }

            }
        });

        // check if the user is being followed or not. If they are, show unfollow
        // if we subsequently unfollow, the button changes to follow





        return convertView;
    }








}


