package com.quebec.app;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;

import com.quebec.services.APICallback;
import com.quebec.services.APIManager;

import java.util.Iterator;
import java.util.List;

/**
 * Created by callum on 01/03/2017.
 */

public class AddUserListFragment extends FriendsListFragment {
    private static String LOG_TAG = AddUserListFragment.class.getSimpleName();
    private Event event;

    public AddUserListFragment() {}

    @Override
    protected String getTitle() {
        return "Add users";
    }


    public static AddUserListFragment newInstance(Event event) {
        AddUserListFragment fragment =  new AddUserListFragment();
        fragment.event = event;
        return fragment;
    }

    @Override
    public void setData() {
        APIManager.getInstance().following(new APICallback<List<User>>() {
            @Override
            public void onSuccess(List<User> users) {
                // Remove those currently at the event
                for (Iterator<User> iterator = users.iterator(); iterator.hasNext();) {
                    User user = iterator.next();
                    if (event.getAttendees().contains(user)) {
                        iterator.remove();
                    }
                }

                setUsers(users);
            }

            @Override
            public void onFailure(String message) {
                Log.d(LOG_TAG, "Couldn't get followers: " + message);
            }
        });
    }

    @Override
    public void setupButton(final Button button, int positon) {
        button.setText("+");
        final User user = getUsers().get(positon);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                APIManager.getInstance().addUserToEvent(event, user.getUserID(), new APICallback<String>() {
                    @Override
                    public void onSuccess(String responseBody) {
                        Log.d(LOG_TAG, "Added user to event");
                    }

                    @Override
                    public void onFailure(String message) {
                        Log.d(LOG_TAG, "Failed to add user to event");
                    }
                });

                adapter.data.remove(user);
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
