package com.quebec.app;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.VideoView;



public class EventDetailFragment extends Fragment {

    public static final String EVENT_KEY = "event_key";

    private String eventID;
    private Event mEvent;

    private TextView eventNameTextView;
    private TextView eventDetailDescription;

    private VideoView eventVideoview;

    private View mFragmentView;
    private GridView gridView;



    private OnEventDetailInteractionListener mListener;

    public EventDetailFragment() {
        // Required empty public constructor
    }


    public static EventDetailFragment newInstance(Event e) {
        EventDetailFragment fragment = new EventDetailFragment();

        Bundle args = new Bundle();
        args.putParcelable(EVENT_KEY, e);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mEvent = getArguments().getParcelable(EVENT_KEY);
        }
        else {
            // TODO: Handle no event passed to the events detail panel.
        }


    }

    /**
     * Creates the fragment view and display the Event information on the page.
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        mFragmentView = inflater.inflate(R.layout.fragment_event_detail, container, false);
        eventNameTextView = (TextView) mFragmentView.findViewById(R.id.eventDetailName);
        eventDetailDescription = (TextView) mFragmentView.findViewById(R.id.eventDetailDescription);
        eventVideoview = (VideoView) mFragmentView.findViewById(R.id.eventVideoView);


        /* If the event has been initialised, then insert the Event information onto the
           the page */
        if (mEvent != null) {
            eventNameTextView.setText(mEvent.getEventName());
            eventDetailDescription.setText(mEvent.getDescription());

            Uri u = Uri.parse("http://clips.vorwaerts-gmbh.de/VfE_html5.mp4");
            eventVideoview.setVideoURI(u);

            eventVideoview.start();
        }

        gridView = (GridView) mFragmentView.findViewById(R.id.eventUsers);

        User[] values = new User[] {
                new User("Andrew Deniszczyc", "123"),
                new User("John Smith", "123"),
                new User("Pete Testing", "123"),
                new User("Evian Water", "123"),
                new User("Nokia Phone", "123")
        };

        EventUsersAdapterItem adapter = new EventUsersAdapterItem(this.getContext(), R.layout.adapter_grid_event_user, values);
        gridView.setAdapter(adapter);

        return mFragmentView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnEventDetailInteractionListener) {
            mListener = (OnEventDetailInteractionListener) context;
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
     * The interface which must be implemented by the related activity.
     */
    public interface OnEventDetailInteractionListener {
        void onBackToEvents();
    }
}
