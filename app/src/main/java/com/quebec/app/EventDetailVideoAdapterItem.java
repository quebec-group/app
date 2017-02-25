package com.quebec.app;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

/**
 * EventUsersTickerAdapterItem displays the users who were present at an event. This ticker is shown in
 * event items on the feeds.
 */

public class EventDetailVideoAdapterItem extends RecyclerView.Adapter<EventDetailVideoAdapterItem.ViewHolder> {
    private Video[] videoData;
    private Context mContext;


    public EventDetailVideoAdapterItem(Context context, Video[] videoData) {
        this.videoData = videoData;
        this.mContext = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public EventDetailVideoAdapterItem.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                     int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_event_detail_video, null);

        ViewHolder viewHolder = new ViewHolder(itemLayoutView);

        VideoView videoView = (VideoView) itemLayoutView.findViewById(R.id.event_detail_video_view);

        // TODO remove the example video.
        Uri u = Uri.parse("http://clips.vorwaerts-gmbh.de/VfE_html5.mp4");
        videoView.setVideoURI(u);

            /* Add scrubbing controls to the video view. */
        MediaController ctrl = new MediaController(mContext);

        videoView.setMediaController(ctrl);
        videoView.start();
        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

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
        return videoData.length;
    }
}

