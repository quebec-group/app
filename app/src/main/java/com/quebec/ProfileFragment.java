package com.quebec;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.amazonaws.mobile.AWSMobileClient;
import com.amazonaws.mobile.user.IdentityManager;

/**
 * Fragment for the Profile view.
 */
public class ProfileFragment extends Fragment {

    private ProfileInteractionListener mListener;

    private IdentityManager identityManager;

    private View mFragmentView;

    /**
     * TextViews on the interface
     */
    private TextView userIdTextView;
    private TextView userNameTextView;

    public ProfileFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Gets the UserIdentity from Amazpn and updates the TextViews.
     *
     */
    private void fetchUserIdentity() {
         // Pre-fetched to avoid race condition where fragment is no longer active.
        final String unknownUserIdentityText =
                getString(R.string.identity_demo_unknown_identity_text);

        AWSMobileClient.defaultMobileClient()
                .getIdentityManager()
                .getUserID(new IdentityManager.IdentityHandler() {

                    @Override
                    public void handleIdentityID(String identityId) {

                        /* Set the user ID to be the identity ID from the user pool. */
                        userIdTextView.setText(identityId);

                        /* If the user is logged in, update the username shown on screen. */
                        if (identityManager.isUserSignedIn()) {
                            userNameTextView.setText(identityManager.getUserName());
                        }
                    }

                    @Override
                    public void handleError(Exception exception) {

                        // We failed to retrieve the user's identity. Set unknown user identifier
                        // in text view.
                        userIdTextView.setText(unknownUserIdentityText);

                    }
                });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        mFragmentView = inflater.inflate(R.layout.fragment_profile, container, false);


        userIdTextView = (TextView) mFragmentView.findViewById(R.id.profileFragment_id);
        userNameTextView = (TextView) mFragmentView.findViewById(R.id.profileFragment_name);

        identityManager = AWSMobileClient.defaultMobileClient().getIdentityManager();

        fetchUserIdentity();

        return mFragmentView;
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


    public interface ProfileInteractionListener {


    }
}
