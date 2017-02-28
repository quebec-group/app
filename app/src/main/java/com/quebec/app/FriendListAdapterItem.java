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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andrew on 03/02/2017.
 */


public class FriendListAdapterItem extends ArrayAdapter<User> implements View.OnClickListener{

    int layoutResourceID;
    List<User> data = new ArrayList<>();
    Context mContext;
    private User current_user;
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
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(layoutResourceID, parent, false);
        }

        User user = data.get(position);

        TextView textViewItem = (TextView) convertView.findViewById(R.id.friendItemName);
        textViewItem.setText(user.getName());

        Button unfollowButton = (Button) convertView.findViewById(R.id.unfollowUser);
        unfollowButton.setOnClickListener(this);
        current_user = user;


        return convertView;
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.unfollowUser:
                unfollow(current_user);
        }
    }

    public void unfollow(final User user) {
        APIManager.getInstance().unfollow(user, new APICallback<String>() {
            @Override
            public void onSuccess(String responseBody) {
                Log.d(LOG_TAG, "Unfollowed:" + user.getUserID());
            }

            @Override
            public void onFailure(String message) {

            }
        });
    }


}


