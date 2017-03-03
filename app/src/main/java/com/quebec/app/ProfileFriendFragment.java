package com.quebec.app;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.amazonaws.mobile.content.ContentItem;
import com.amazonaws.mobile.content.ContentProgressListener;
import com.makeramen.roundedimageview.RoundedImageView;
import com.quebec.services.APICallback;
import com.quebec.services.APIManager;

import java.util.List;



public class ProfileFriendFragment extends Fragment implements View.OnClickListener{


    private User user;

    private static final String USER_KEY = "user_key";
    private OnFragmentInteractionListener mListener;
    private ProfileFragment.ProfileInteractionListener mProfileListener;
    private static String LOG_TAG = ProfileFriendFragment.class.getSimpleName();

    private View mFragmentView;
    private boolean following;
    private TextView followingCount;
    private TextView followersCount;
    private TextView eventsCount;
    private TextView profileUserName;
    private RoundedImageView profile_picture_view;



    public ProfileFriendFragment() {
        // Required empty public constructor
    }


    public static ProfileFriendFragment newInstance(User u) {
        ProfileFriendFragment fragment = new ProfileFriendFragment();
        Bundle args = new Bundle();

        args.putParcelable(USER_KEY, u);
        fragment.setArguments(args);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            user = getArguments().getParcelable(USER_KEY);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mFragmentView = inflater.inflate(R.layout.fragment_profile_friend, container, false);


        // Update the profile picture on screen.
        profile_picture_view = (RoundedImageView) mFragmentView.findViewById(R.id.profile_picture_view);

        final Button follow = (Button) mFragmentView.findViewById(R.id.profile_friend_follow);
        follow.setOnClickListener(this);

        followingCount = (TextView) mFragmentView.findViewById(R.id.profileFollowingCount);
        followersCount = (TextView) mFragmentView.findViewById(R.id.profileFollowersCount);
        eventsCount = (TextView) mFragmentView.findViewById(R.id.profileEventsCount);

        followingCount = (TextView) mFragmentView.findViewById(R.id.profileFollowingCount);
        followersCount = (TextView) mFragmentView.findViewById(R.id.profileFollowersCount);
        eventsCount = (TextView) mFragmentView.findViewById(R.id.profileEventsCount);

        profileUserName = (TextView) mFragmentView.findViewById(R.id.profileUserName);
        profileUserName.setText(user.getName());

        getStats();

        mFragmentView.findViewById(R.id.followingLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProfileListener.openFollowingList();
            }
        });

        mFragmentView.findViewById(R.id.followersLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProfileListener.openFollowersList();
            }
        });

        APIManager.getInstance().iFollow(user, new APICallback<Boolean>() {
            @Override
            public void onSuccess(Boolean responseBody) {
                if(responseBody) {
                    follow.setText("Unfollow");
                    user.setiFollow(true);
                    
                } else {
                    follow.setText("Follow");
                    user.setiFollow(false);
                }
                Log.d("Event users adapter", user.getUserID() + " " + user.doIFollow());
            }

            @Override
            public void onFailure(String message) {

            }
        });


        setProfilePicture();
        /* Initiate the events feed on the profile, by loading the data into the adapter view. */
        // TODO: Replace stubs with actual Events


        final RecyclerView mRecyclerView = (RecyclerView) mFragmentView.findViewById(R.id.profileEventsFeedRecycler);
        boolean b = mRecyclerView.hasFixedSize();

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        final Context context = this.getContext();

        final ProgressDialog spinner = ProgressDialog.show(getContext(), "Loading", "Wait while loading...");


        APIManager.getInstance().getAttendedEvents(user.getUserID(), new APICallback<List<Event>>() {
            @Override
            public void onSuccess(final List<Event> events) {

                spinner.dismiss();
                final String count = events.size()+"";
                eventsCount.setText(count);

                EventListAdapterItem mAdapter = new EventListAdapterItem(events, context);

                mAdapter.setOnItemClickListener(new EventListAdapterItem.EventItemClickInterface() {
                    @Override
                    public void onItemClick(int position, View v) {
                        mListener.onProfileFriendEventSelected(events.get(position));
                    }
                });

                mRecyclerView.setAdapter(mAdapter);

            }

            @Override
            public void onFailure(String message) {
                spinner.dismiss();
            }
        });

        return mFragmentView;
    }

    private void setProfilePicture() {
        user.getProfilePicture(new ContentProgressListener() {
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
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
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

    public void getStats() {
        APIManager.getInstance().followersCount(user.getUserID(), new APICallback<Integer>() {
            @Override
            public void onSuccess(Integer responseBody) {
                final String count = responseBody.toString();
                followersCount.setText(count);
            }

            @Override
            public void onFailure(String message) {

            }
        });

        APIManager.getInstance().followingCount(user.getUserID(), new APICallback<Integer>() {
            @Override
            public void onSuccess(Integer responseBody) {
                final String count = responseBody.toString();
                followingCount.setText(count);
            }

            @Override
            public void onFailure(String message) {

            }
        });


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.profile_friend_follow:
                final Button follow = (Button) mFragmentView.findViewById(R.id.profile_friend_follow);
                // Give user ability to follow/unfollow user dependent on current state
                if(follow.getText() == "Unfollow") {
                    APIManager.getInstance().unfollow(user, new APICallback<String>() {
                        @Override
                        public void onSuccess(String responseBody) {
                            follow.setText("Follow");
                            getStats();
                            Log.d(LOG_TAG, "Unfollowed: " + user.getUserID());
                        }

                        @Override
                        public void onFailure(String message) {
                            Log.d(LOG_TAG, "Failed to unfollow: " + user.getUserID());
                        }
                    });
                } else {
                    APIManager.getInstance().follow(user, new APICallback<String>() {
                        @Override
                        public void onSuccess(String responseBody) {
                            follow.setText("Unfollow");
                            getStats();
                            Log.d(LOG_TAG, "Followed: " + user.getUserID());
                        }

                        @Override
                        public void onFailure(String message) {
                            Log.d(LOG_TAG, "Failed to follow: " + user.getUserID());
                        }
                    });
                }
        }
    }

    public interface OnFragmentInteractionListener {
        void onProfileFriendEventSelected(Event event);
    }

    public static interface FollowStatusCallBack {
        public void onResponseReceived(Boolean follows);
    }

}
