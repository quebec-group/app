package com.quebec.app;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.amazonaws.mobile.content.ContentItem;
import com.amazonaws.mobile.content.ContentProgressListener;
import com.makeramen.roundedimageview.RoundedImageView;
import com.quebec.services.APICallback;
import com.quebec.services.APIManager;

import java.util.List;

/**
 * Created by Andrew on 06/02/2017.
 */

public class EventUsersAdapterItem extends ArrayAdapter {
    int layoutResourceID;
    List<User> data = null;
    Context mContext;


    public EventUsersAdapterItem(Context mContext, int layoutResourceID, List<User> objects) {
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

        final User user = data.get(position);



        TextView textViewUserName = (TextView) convertView.findViewById(R.id.event_user_name);
        textViewUserName.setText(user.getName());


        final RoundedImageView imageView = (RoundedImageView) convertView.findViewById(R.id.event_user_image);

        user.getProfilePicture(new ContentProgressListener() {
            @Override
            public void onSuccess(ContentItem contentItem) {
                imageView.setImageURI(Uri.fromFile(contentItem.getFile()));
            }

            @Override
            public void onProgressUpdate(String filePath, boolean isWaiting, long bytesCurrent, long bytesTotal) {

            }

            @Override
            public void onError(String filePath, Exception ex) {

            }
        });

        return convertView;
    }
}
