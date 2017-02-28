package com.quebec.app;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.quebec.services.Video;

import java.util.ArrayList;

/**
 * EventUsersTickerAdapterItem displays the users who were present at an event. This ticker is shown in
 * event items on the feeds.
 */

public class EventDetailVideoAdapterItem extends RecyclerView.Adapter<EventDetailVideoAdapterItem.ViewHolder> implements View.OnClickListener {
    private ArrayList<Video> videoData;
    private Context mContext;
    private Activity mActivity;


    public EventDetailVideoAdapterItem(Context context, Activity activity, ArrayList<Video> videoData) {
        this.videoData = videoData;
        this.mContext = context;
        this.mActivity = activity;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public EventDetailVideoAdapterItem.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                     int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_event_detail_video, null);

        ViewHolder viewHolder = new ViewHolder(itemLayoutView);

        Button button = (Button) itemLayoutView.findViewById(R.id.event_detail_video_btn);
        button.setOnClickListener(this);

        return viewHolder;
    }


    public void onShowPopup() {
        EventDetailVideoDialog dialog = new EventDetailVideoDialog(this.mContext, this.mActivity);
        dialog.show();
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

    }

    @Override
    public void onClick(View v) {
        onShowPopup();
    }

    // inner class to hold a reference to each item of RecyclerView
    public static class ViewHolder extends RecyclerView.ViewHolder {


        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);

        }
    }


    // Return the size of your itemsData (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return videoData.size();
    }
}


