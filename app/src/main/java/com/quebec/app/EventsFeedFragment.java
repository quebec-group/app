package com.quebec.app;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.quebec.services.APICallback;
import com.quebec.services.APIManager;

import java.util.ArrayList;
import java.util.List;


public class EventsFeedFragment extends Fragment {
    private static String LOG_TAG = EventsFeedFragment.class.getSimpleName();
    private EventsFeedInteractionListener mListener;


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

        final RecyclerView mRecyclerView = (RecyclerView) v.findViewById(R.id.eventsFeedRecycler);
        mRecyclerView.hasFixedSize();

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        final Context context = this.getContext();

        final ProgressDialog spinner = new ProgressDialog(this.getContext());
        spinner.show();

        APIManager.getInstance().getEvents(new APICallback<List<Event>>() {
            @Override
            public void onSuccess(final List<Event> events) {

                spinner.dismiss();

                EventListAdapterItem mAdapter = new EventListAdapterItem(events, context);

                mAdapter.setOnItemClickListener(new EventListAdapterItem.EventItemClickInterface() {
                    @Override
                    public void onItemClick(int position, View v) {
                        mListener.onEventSelected(events.get(position));
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
        super.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    /**
     * InteractionListener which is implemented by the parent Activity
     */
    public interface EventsFeedInteractionListener {
        void onEventSelected(Event e);
    }



}
