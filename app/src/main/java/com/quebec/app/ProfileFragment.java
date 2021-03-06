package com.quebec.app;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.amazonaws.mobile.AWSMobileClient;
import com.amazonaws.mobile.content.ContentItem;
import com.amazonaws.mobile.content.ContentProgressListener;
import com.amazonaws.mobile.user.IdentityManager;
import com.makeramen.roundedimageview.RoundedImageView;
import com.quebec.app.auth.SplashActivity;
import com.quebec.services.APICallback;
import com.quebec.services.APIManager;

import java.io.File;
import java.util.List;

/**
 * ProfileFragment is the fragment for the profile view.
 */
public class ProfileFragment extends Fragment implements View.OnClickListener  {


    private static final String LOG_TAG = "log tag";
    private ProfileInteractionListener mListener;

    private IdentityManager identityManager;

    /** This fragment's view. */
    private View mFragmentView;

    /* Elements on the page. */
    private View dropdown_button;
    private TextView userNameTextView;
    private RoundedImageView profile_picture_view;

    private TextView followingCount;
    private TextView followersCount;
    private TextView eventsCount;
    private User mUser;

    public ProfileFragment() {
        // Required empty public constructor
    }


    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
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
                            userNameTextView.setText(identityManager.getUserName());
                            userNameTextView.setSelected(true);
                        }
                    }

                    @Override
                    public void handleError(Exception exception) {}

                });



        /* Get the profile picture. */
        ProfilePictureHandler handler = new ProfilePictureHandler();
        handler.getUserProfilePicture(mUser, new ContentProgressListener() {
            @Override
            public void onSuccess(ContentItem contentItem) {
                File f = contentItem.getFile();
                Bitmap bitmap = BitmapFactory.decodeFile(f.getPath());
                profile_picture_view.setImageBitmap(bitmap);
            }

            @Override
            public void onProgressUpdate(String filePath, boolean isWaiting, long bytesCurrent, long bytesTotal) {}

            @Override
            public void onError(String filePath, Exception ex) {}
        });

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        mFragmentView = inflater.inflate(R.layout.fragment_profile, container, false);

        userNameTextView = (TextView) mFragmentView.findViewById(R.id.profileFragment_name);
        profile_picture_view = (RoundedImageView) mFragmentView.findViewById(R.id.profile_picture_view);

        /* Text views for the profile header. */
        followingCount = (TextView) mFragmentView.findViewById(R.id.profileFollowingCount);
        followersCount = (TextView) mFragmentView.findViewById(R.id.profileFollowersCount);
        eventsCount = (TextView) mFragmentView.findViewById(R.id.profileEventsCount);


        /* Declare the onclick event handlers for the buttons. */
        dropdown_button = (View) mFragmentView.findViewById(R.id.profile_dropdown_button);

        /* Sets the event listeners for the dropdown button. */
        dropdown_button.setOnClickListener(this);

        identityManager = AWSMobileClient.defaultMobileClient()
                .getIdentityManager();

        mFragmentView.findViewById(R.id.followingLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.openFollowingList();
            }
        });

        mFragmentView.findViewById(R.id.followersLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.openFollowersList();
            }
        });

        final RecyclerView mRecyclerView = (RecyclerView) mFragmentView.findViewById(R.id.profileEventsFeedRecycler);
        mRecyclerView.hasFixedSize();

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        final Context context = this.getContext();

        final ProgressDialog spinner = ProgressDialog.show(getContext(), "Loading", "Wait while loading...");


        /*  Gets the currently logged in user. */
        APIManager.getInstance().getInfo(new APICallback<User>() {
            @Override
            public void onSuccess(User responseBody) {
                mUser = responseBody;
                setProfilePicture();
                getStats();
            }

            @Override
            public void onFailure(String message) {
                // TODO: Handle the failure state.
            }
        });

        /* Get the events related to the user. */
        final TextView finalEventsCount = eventsCount;
        APIManager.getInstance().getAttendedEvents(new APICallback<List<Event>>() {
            @Override
            public void onSuccess(final List<Event> events) {
                finalEventsCount.setText(String.valueOf(events.size()));

                spinner.dismiss();

                EventListAdapterItem mAdapter = new EventListAdapterItem(events, context);

                mAdapter.setOnItemClickListener(new EventListAdapterItem.EventItemClickInterface() {
                    @Override
                    public void onItemClick(int position, View v) {
                        mListener.onProfileEventSelected(events.get(position));
                    }
                });

                mRecyclerView.setAdapter(mAdapter);

            }

            @Override
            public void onFailure(String message) {
                Log.e(LOG_TAG, "Failed to get events: " + message);
                spinner.dismiss();
            }
        });

        /* Get the logged in user information and display this information on the page. */
        fetchUserIdentity();

        return mFragmentView;
    }

    /**
     * Show the profile picture on the page.
     */
    private void setProfilePicture() {
        mUser.getProfilePicture(new ContentProgressListener() {
            @Override
            public void onSuccess(ContentItem contentItem) {
                profile_picture_view.setImageURI(Uri.fromFile(contentItem.getFile()));
            }

            @Override
            public void onProgressUpdate(String filePath, boolean isWaiting, long bytesCurrent, long bytesTotal) {

            }

            @Override
            public void onError(String filePath, Exception ex) {
                Log.e(LOG_TAG, "Error getting " + filePath, ex);
            }
        });
    }


    /**
     * Gets the statistics for the user.
     */
    public void getStats() {
        APIManager.getInstance().followersCount(mUser.getUserID(), new APICallback<Integer>() {
            @Override
            public void onSuccess(Integer responseBody) {
                followersCount.setText(responseBody.toString());
            }

            @Override
            public void onFailure(String message) {}
        });

        APIManager.getInstance().followingCount(mUser.getUserID(), new APICallback<Integer>() {
            @Override
            public void onSuccess(Integer responseBody) {
                followingCount.setText(responseBody.toString());
            }

            @Override
            public void onFailure(String message) {}
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.profile_dropdown_button:
                openDropdown();
                break;
        }
    }

    /**
     * Setup the dropdown navigation for use with the extra navigation links.
     */
    private void openDropdown() {

        PopupMenu popup = new PopupMenu(this.getContext(), dropdown_button);
        //Inflating the Popup using xml file
        popup.getMenuInflater().inflate(R.menu.profile_menu_dropdown, popup.getMenu());

        // Setp the popup to handle on click events.
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.profile_menu_dropdown_logout:
                        logoutAccount();
                        break;
                    case R.id.profile_menu_dropdown_update_picture:
                        openProfilePictureUpdate();
                        break;
                    case R.id.profile_menu_dropdown_training:
                        openVideoTraining();
                        break;
                }
                return false;
            }


        });

        popup.show();

    }

    /**
     * Open the video training update page.
     */
    private void openVideoTraining() {
        Intent intent = new Intent(this.getContext(), SignUpVideoActivity.class);
        startActivity(intent);
    }

    /**
     * Open the profile picture update page.
     */
    private void openProfilePictureUpdate() {
        Intent intent = new Intent(this.getContext(), ProfilePictureUpdateActivity.class);
        startActivity(intent);
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

        // Should clear the back stack after returning to the splash screen.
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


    public interface ProfileInteractionListener {
        void openFollowingList();
        void openFollowersList();
        void openAboutPage();
        void updateProfilePictureActivity();
        void onProfileEventSelected(Event e);
    }
}
