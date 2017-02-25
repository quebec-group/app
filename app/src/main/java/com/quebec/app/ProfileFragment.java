package com.quebec.app;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;


import com.amazonaws.mobile.AWSMobileClient;
import com.amazonaws.mobile.content.ContentItem;
import com.amazonaws.mobile.content.ContentProgressListener;
import com.amazonaws.mobile.user.IdentityManager;
import com.makeramen.roundedimageview.RoundedImageView;
import com.quebec.app.auth.SplashActivity;

import java.io.File;
import java.util.ArrayList;

/**
 * ProfileFragment is the fragment for the profile view.
 */
public class ProfileFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener  {


    private ProfileInteractionListener mListener;

    private IdentityManager identityManager;

    /** This fragment's view. */
    private View mFragmentView;

    private TextView userNameTextView;
    private ListView profileEventsFeed;
    private RoundedImageView profile_picture_view;


    public ProfileFragment() {
        // Required empty public constructor
    }


    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        Intent intent = getActivity().getIntent();

        // TODO: complete the refresh of the view when the profile picture is updated.
        // String message = intent.getStringExtra(.EXTRA_MESSAGE);

        super.onCreate(savedInstanceState);
    }

    private void fetchUserIdentity() {
         // Pre-fetched to avoid race condition where fragment is no longer active.
        final String unknownUserIdentityText =
                getString(R.string.identity_demo_unknown_identity_text);

        AWSMobileClient.defaultMobileClient()
                .getIdentityManager()
                .getUserID(new IdentityManager.IdentityHandler() {

                    @Override
                    public void handleIdentityID(String identityId) {
                        if (identityManager.isUserSignedIn()) {
                            // TODO: handle overflow of a username - could be done with a scrolling textview
                            userNameTextView.setText(identityManager.getUserName());
                            userNameTextView.setSelected(true);
                        }
                    }

                    @Override
                    public void handleError(Exception exception) {
                        // TODO: user could not be found - do we retry or log the user out?
                    }

                });


        /* Get the profile picture. */
        ProfilePictureHandler handler = new ProfilePictureHandler();
        handler.getImage(new ContentProgressListener() {
            @Override
            public void onSuccess(ContentItem contentItem) {
                File f = contentItem.getFile();
                Bitmap bitmap = BitmapFactory.decodeFile(f.getPath());
                profile_picture_view.setImageBitmap(bitmap);
            }

            @Override
            public void onProgressUpdate(String filePath, boolean isWaiting, long bytesCurrent, long bytesTotal) {}

            @Override
            public void onError(String filePath, Exception ex) {
                // TODO profile picture could not be loaded and hence show the placeholder image.
            }
        });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        mFragmentView = inflater.inflate(R.layout.fragment_profile, container, false);


        userNameTextView = (TextView) mFragmentView.findViewById(R.id.profileFragment_name);
        profile_picture_view = (RoundedImageView) mFragmentView.findViewById(R.id.profile_picture_view);

        /* Declare the onclick event handlers for the buttons. */
        Button b1 = (Button) mFragmentView.findViewById(R.id.button_friends_list);
        Button b2 = (Button) mFragmentView.findViewById(R.id.profile_logout);
        Button b3 = (Button) mFragmentView.findViewById(R.id.profile_update_details_button);

        b1.setOnClickListener(this);
        b2.setOnClickListener(this);
        b3.setOnClickListener(this);

        identityManager = AWSMobileClient.defaultMobileClient()
                .getIdentityManager();


        profileEventsFeed = (ListView) mFragmentView.findViewById(R.id.profileEventsFeedList);

        /* Initiate the events feed on the profile, by loading the data into the adapter view. */
        // TODO: Replace stubs with actual Events
//        Event[] values = new Event[] {
//                new Event("Andrew's Networking Event", "An evening of networking and getting to know each other over lots of drinks and good food.", "123", "cambridge", "13:03", "asd", new ArrayList<User>()),
//                new Event("Andrew's Networking Event", "An evening of networking and getting to know each other over lots of drinks and good food.", "123", "cambridge", "13:03", "asd", new ArrayList<User>()),
//        };
//
//        EventListAdapterItem adapter = new EventListAdapterItem(this.getContext(), R.layout.adapter_event_item, values);
//        profileEventsFeed.setAdapter(adapter);
        profileEventsFeed.setOnItemClickListener(this);

        /* Get the logged in user information and display this information on the page. */
        fetchUserIdentity();

        return mFragmentView;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.profile_logout:
                logoutAccount();
                break;
            case R.id.button_friends_list:
                mListener.openFriendsList();
                break;
            case R.id.profile_update_details_button:
                mListener.updateProfilePictureActivity();
                break;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ProfileInteractionListener) {
            mListener = (ProfileInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    /**
     * Log out of the user account, then show the SplashActivity.
     */
    public void logoutAccount() {
        AWSMobileClient.defaultMobileClient()
                .getIdentityManager()
                .signOut();

        Intent intent = new Intent(this.getContext(), SplashActivity.class);
        startActivity(intent);
    }

    /**
     * Responds to item clicks on the events feed on the profile view.
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        profileEventsFeed.setItemChecked(position, true);
        Event e = (Event) profileEventsFeed.getItemAtPosition(position);

        // Call the interaction listener with the Event object.
        mListener.onProfileEventSelected(e);
    }

    public interface ProfileInteractionListener {
        void openFriendsList();
        void updateProfilePictureActivity();
        void onProfileEventSelected(Event e);
    }
}
