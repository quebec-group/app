package com.quebec.app;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andrew on 03/02/2017.
 */


public class FriendListAdapterItem extends ArrayAdapter<User>{

    private final FriendsListFragment fragment;
    int layoutResourceID;
    List<User> data = new ArrayList<>();
    Context mContext;
    private static String LOG_TAG = FriendListAdapterItem.class.getSimpleName();


    public FriendListAdapterItem(FriendsListFragment fragment, int layoutResourceID, List<User> objects) {
        super(fragment.getContext(), layoutResourceID, objects);

        this.layoutResourceID = layoutResourceID;
        this.mContext = fragment.getContext();
        this.fragment = fragment;
        this.data = objects;

    }

    public FriendListAdapterItem(FriendsListFragment fragment, int layoutResourceID) {
        this(fragment, layoutResourceID, new ArrayList<User>());
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

        final Button button = (Button) convertView.findViewById(R.id.followToggleButton);

        fragment.setupButton(button, position);

        return convertView;
    }
}


