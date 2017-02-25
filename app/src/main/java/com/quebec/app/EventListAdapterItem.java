package com.quebec.app;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Andrew on 03/02/2017.
 */

public class EventListAdapterItem extends ArrayAdapter<Event> {

    int layoutResourceID;
    List<Event> data;
    Context mContext;


    public EventListAdapterItem(Context mContext, int layoutResourceID, List<Event> objects) {
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

        Event event = data.get(position);

        TextView textViewItem = (TextView) convertView.findViewById(R.id.eventItemName);
        TextView textViewDescriptionItem = (TextView) convertView.findViewById(R.id.eventItemDescription);

        textViewItem.setText(event.getEventName());
//        textViewDescriptionItem.setText(event.getDescription());


        /* Setup the event ticker, by adding the users associated with the event. */

        EventUsersTickerAdapterItem adapter = new EventUsersTickerAdapterItem(event.getAttendees());

        /* Makes use of the RecyclerView for the horizontal scrolling field. */
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.mContext ,LinearLayoutManager.HORIZONTAL, false);
        RecyclerView eventItemTicker = (RecyclerView) convertView.findViewById(R.id.eventItemTicker);
        eventItemTicker.setLayoutManager(layoutManager);
        eventItemTicker.setAdapter(adapter);

        return convertView;
    }
}
