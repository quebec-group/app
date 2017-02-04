package com.quebec;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;


public class FriendsListFragment extends Fragment implements AdapterView.OnItemClickListener {


    private FriendsListInteractionHandler mListener;

    ListView listView;

    public FriendsListFragment() {
        // Required empty public constructor
    }

    public static FriendsListFragment newInstance() {
        FriendsListFragment fragment = new FriendsListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_friends_list, container, false);

        listView = (ListView) v.findViewById(R.id.friendsList);

        User[] values = new User[] {
                new User("hello"),
                new User("hello2")
        };

        FriendListAdapterItem adapter = new FriendListAdapterItem(this.getContext(), R.layout.adapter_friend_list_item, values);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FriendsListInteractionHandler) {
            mListener = (FriendsListInteractionHandler) context;
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
     * Click event for the list elements
     * @param adapterView
     * @param view
     * @param position
     * @param l
     */
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        listView.setItemChecked(position, true);
        User u = (User) listView.getItemAtPosition(position);

        mListener.onFriendSelected(u);
    }



    public interface FriendsListInteractionHandler {
        // TODO: Update argument type and name
        void onFriendSelected(User u);
    }
}
