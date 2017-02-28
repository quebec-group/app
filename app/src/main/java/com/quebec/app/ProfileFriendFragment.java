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

import com.makeramen.roundedimageview.RoundedImageView;
import com.quebec.services.APICallback;
import com.quebec.services.APIManager;

import java.util.List;


public class ProfileFriendFragment extends Fragment {

    private User user;

    private static final String USER_KEY = "user_key";
    private OnFragmentInteractionListener mListener;

    private View mFragmentView;

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

        // Get the User's information and display on the page
        TextView nameField = (TextView) mFragmentView.findViewById(R.id.profile_friend_name);
        nameField.setText(user.getName());

        // Update the profile picture on screen.
        RoundedImageView imageField = (RoundedImageView) mFragmentView.findViewById(R.id.profile_friend_picture_view);

        /* Check if the user profile picture is set. */
        if (!(user.getProfileID()).equals("")) {
            Uri imageUri = Uri.parse(user.getProfileID());
            imageField.setImageURI(imageUri);
        }

        /* Initiate the events feed on the profile, by loading the data into the adapter view. */
        // TODO: Replace stubs with actual Events


        final RecyclerView mRecyclerView = (RecyclerView) mFragmentView.findViewById(R.id.profileFriendEventsRecycler);
        boolean b = mRecyclerView.hasFixedSize();

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        final Context context = this.getContext();

        final ProgressDialog spinner = ProgressDialog.show(getContext(), "Loading", "Wait while loading...");

        // TOOO
        APIManager.getInstance().getEvents(new APICallback<List<Event>>() {
            @Override
            public void onSuccess(final List<Event> events) {

                spinner.dismiss();

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

    public interface OnFragmentInteractionListener {
        void onProfileFriendEventSelected(Event event);
    }
}
