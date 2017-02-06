package com.quebec;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;


public class FriendsListFragment extends Fragment implements AdapterView.OnItemClickListener {


    private FriendsListInteractionHandler mListener;

    private EditText friendsListSearchBox;
    private ListView listView;

    private FriendListAdapterItem adapter;
    private User[] values;

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

        values = new User[] {
                new User("hello"),
                new User("hello2")
        };

        adapter = new FriendListAdapterItem(this.getContext(), R.layout.adapter_friend_list_item, values);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);


        // Setup the search field for the friends page.
        friendsListSearchBox = (EditText) v.findViewById(R.id.friendsListSearchBox);

        friendsListSearchBox.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    performSearch();
                    return true;
                }
                return false;
            }
        });

        friendsListSearchBox.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction()==KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    performSearch();
                    return true;
                }
                performSearch();
                return false;
            }
        });

        return v;
    }

    public void performSearch() {
        String request = friendsListSearchBox.getText().toString();

        Log.e("JIJJ", "msg");
        // TODO Make request to the server for search

        User[] newValues = new User[] {
                new User("change 1"),
                new User("change 2")
        };

        adapter.clear();
        adapter.addAll(newValues);
        adapter.notifyDataSetChanged();

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
