package com.quebec.app;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.amazonaws.mobile.content.ContentItem;
import com.amazonaws.mobile.content.ContentProgressListener;
import com.makeramen.roundedimageview.RoundedImageView;

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

        /* Load the profile picture into the view. */
        final RoundedImageView friendItemImage = (RoundedImageView) convertView.findViewById(R.id.friendItemImage);
        
        user.getProfilePicture(new ContentProgressListener() {
            @Override
            public void onSuccess(ContentItem contentItem) {
                friendItemImage.setImageURI(Uri.fromFile(contentItem.getFile()));
            }

            @Override
            public void onProgressUpdate(String filePath, boolean isWaiting, long bytesCurrent, long bytesTotal) {}

            @Override
            public void onError(String filePath, Exception ex) {}
        });

        final Button button = (Button) convertView.findViewById(R.id.followToggleButton);

        fragment.setupButton(button, position);
        fragment.setupListItem(convertView, position);
        return convertView;
    }
}


