package com.quebec.app;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.quebec.services.Video;

import java.util.ArrayList;


public class EventsFeedFragment extends Fragment implements AdapterView.OnItemClickListener {
    private static String LOG_TAG = EventsFeedFragment.class.getSimpleName();
    private EventsFeedInteractionListener mListener;

    private PopupWindow popWindow;

    private Parcelable listViewState;
    private ListView listView;


    public EventsFeedFragment() {

    }

    public static EventsFeedFragment newInstance() {
        EventsFeedFragment fragment = new EventsFeedFragment();
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
        View v = inflater.inflate(R.layout.fragment_events_feed, container, false);

        listView = (ListView) v.findViewById(R.id.eventsFeedList);

        if (listViewState != null) {
            listView.onRestoreInstanceState(listViewState);
        }

        // TODO: Replace stubs with actual Events

        /*
        APIManager.getInstance().getEvents(new APICallback<List<Event>>() {
            @Override
            public void onSuccess(List<Event> events) {

                EventListAdapterItem adapter = new EventListAdapterItem(EventsFeedFragment.this.getContext(), R.layout.adapter_event_item, events);
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(EventsFeedFragment.this);
            }

            @Override
            public void onFailure(String message) {
                Log.d(LOG_TAG, message);
            }
        });
        */

        ArrayList<Event> events = new ArrayList<>();
        events.add(new Event("Andrew's networking event", 123, "London", "25/06/2017", new ArrayList<Video>(), new ArrayList<User>(), false, 15));


        EventListAdapterItem adapter = new EventListAdapterItem(EventsFeedFragment.this.getContext(), R.layout.adapter_event_item, events);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(EventsFeedFragment.this);

        return v;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof EventsFeedInteractionListener) {
            mListener = (EventsFeedInteractionListener) context;
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

    @Override
    public void onPause() {
        listViewState = listView.onSaveInstanceState();
        super.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }


    /**
     * Click event for the list elements.
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {

        listView.setItemChecked(position, true);
        Event e = (Event) listView.getItemAtPosition(position);

        // Call the interaction listener with the Event object.
        mListener.onEventSelected(e);

    }

    /**
     * InteractionListener which is implemented by the parent Activity
     */
    public interface EventsFeedInteractionListener {
        void onEventSelected(Event e);
    }



}
