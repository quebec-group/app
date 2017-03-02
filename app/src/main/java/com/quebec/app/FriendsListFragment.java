package com.quebec.app;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.quebec.services.APICallback;
import com.quebec.services.APIManager;

import java.util.List;


public abstract class FriendsListFragment extends Fragment implements AdapterView.OnItemClickListener {
    private static String LOG_TAG = FriendsListFragment.class.getSimpleName();

    private FriendsListInteractionHandler mListener;

    private EditText friendsListSearchBox;
    private ListView listView;

    protected FriendListAdapterItem adapter;

    public FriendsListFragment() {
        // Required empty public constructor
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

        adapter = new FriendListAdapterItem(this, R.layout.adapter_friend_list_item);

        TextView titleView = (TextView) v.findViewById(R.id.friends_fragment_title);
        titleView.setText(getTitle());

        listView = (ListView) v.findViewById(R.id.friendsList);

        listView.setAdapter(adapter);

        setData();

        listView.setOnItemClickListener(this);

        // Setup the search field for the friends page.
        friendsListSearchBox = (EditText) v.findViewById(R.id.friendsListSearchBox);

        friendsListSearchBox.setImeOptions(EditorInfo.IME_ACTION_SEARCH);

        friendsListSearchBox.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    performSearch();
                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });


        return v;
    }

    protected abstract String getTitle();

    public abstract void setData();

    public void performSearch() {
        String request = friendsListSearchBox.getText().toString();

        APIManager.getInstance().find(request, new APICallback<List<User>>() {
            @Override
            public void onSuccess(List<User> users) {
                setUsers(users);
            }

            @Override
            public void onFailure(String message) {
                Log.e(LOG_TAG, "Error searching: " + message);
            }
        });
    }

    protected void setUsers(List<User> users) {
        adapter.clear();
        adapter.addAll(users);
        adapter.notifyDataSetChanged();
    }

    protected List<User> getUsers() {
        return adapter.data;
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
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        listView.setItemChecked(position, true);
        User u = (User) listView.getItemAtPosition(position);

        mListener.onFriendSelected(u);
    }

    public abstract void setupButton(Button button, int positon);


    public interface FriendsListInteractionHandler {
        // TODO: Update argument type and name
        void onFriendSelected(User u);
    }
}
