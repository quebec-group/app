package com.quebec.app;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.quebec.services.Video;

import java.util.ArrayList;

/**
 * EventUsersTickerAdapterItem displays the users who were present at an event. This ticker is shown in
 * event items on the feeds.
 */

public class EventDetailVideoAdapterItem extends RecyclerView.Adapter<EventDetailVideoAdapterItem.ViewHolder> {
    private ArrayList<Video> videoData;

    private static EventDetailVideoItemClickInterface mListener;

    public EventDetailVideoAdapterItem(ArrayList<Video> videoData) {
        this.videoData = videoData;

    }

    // Create new views (invoked by the layout manager)
    @Override
    public EventDetailVideoAdapterItem.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                     int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_event_detail_video, null);

        ViewHolder viewHolder = new ViewHolder(itemLayoutView);

        return viewHolder;
    }


    public void setOnItemClickListener(EventDetailVideoItemClickInterface mListener) {
        this.mListener = mListener;
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

    }

    // inner class to hold a reference to each item of RecyclerView
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);

            ImageView image = (ImageView) itemLayoutView.findViewById(R.id.eventVideoImage);
            image.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mListener.onItemClick(getAdapterPosition(), v);
        }

    }


    // Return the size of your itemsData (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return videoData.size();
    }

    public interface EventDetailVideoItemClickInterface {
        void onItemClick(int position, View v);
    }
}


