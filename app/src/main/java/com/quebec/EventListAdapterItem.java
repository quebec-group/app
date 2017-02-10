package com.quebec;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by Andrew on 03/02/2017.
 */

public class EventListAdapterItem extends ArrayAdapter<Event> {

    int layoutResourceID;
    Event data[] = null;
    Context mContext;


    public EventListAdapterItem(Context mContext, int layoutResourceID, Event[] objects) {
        super(mContext, layoutResourceID, objects);

        this.layoutResourceID = layoutResourceID;
        this.mContext = mContext;
        this.data = objects;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(layoutResourceID, parent, false);
        }

        Event event = data[position];

        TextView textViewItem = (TextView) convertView.findViewById(R.id.eventItemName);
        TextView textViewDescriptionItem = (TextView) convertView.findViewById(R.id.eventItemDescription);

        textViewItem.setText(event.getName());
        textViewDescriptionItem.setText(event.getDescription());
        return convertView;
    }
}
