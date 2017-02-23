package com.quebec.app;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


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

    }
}
