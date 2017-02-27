package com.quebec.app;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Andrew on 03/02/2017.
 */


public class FriendListAdapterItem extends ArrayAdapter<User> {

    int layoutResourceID;
    ArrayList<User> data = new ArrayList<>();
    Context mContext;


    public FriendListAdapterItem(Context mContext, int layoutResourceID, ArrayList<User> objects) {
        super(mContext, layoutResourceID, objects);

        this.layoutResourceID = layoutResourceID;
        this.mContext = mContext;
        this.data = objects;

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


        return convertView;
    }
}
