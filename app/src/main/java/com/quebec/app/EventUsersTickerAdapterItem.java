
package com.quebec.app;


import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amazonaws.mobile.content.ContentItem;
import com.amazonaws.mobile.content.ContentProgressListener;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

/**
 * EventUsersTickerAdapterItem displays the users who were present at an event. This ticker is shown in
 * event items on the feeds.
 */

public class EventUsersTickerAdapterItem extends RecyclerView.Adapter<EventUsersTickerAdapterItem.ViewHolder> {
    private List<User> usersData;

    public EventUsersTickerAdapterItem(List<User> usersData) {
        this.usersData = usersData;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public EventUsersTickerAdapterItem.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_profile_image_ticker, null);

        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        final RoundedImageView photoView = (RoundedImageView) viewHolder.itemView.findViewById(R.id.event_ticker_user_image);


        usersData.get(position).getProfilePicture(new ContentProgressListener() {
            @Override
            public void onSuccess(ContentItem contentItem) {
                photoView.setImageURI(Uri.fromFile(contentItem.getFile()));
            }

            @Override
            public void onProgressUpdate(String filePath, boolean isWaiting, long bytesCurrent, long bytesTotal) {

            }

            @Override
            public void onError(String filePath, Exception ex) {

            }
        });
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
        return usersData.size();
    }
}

