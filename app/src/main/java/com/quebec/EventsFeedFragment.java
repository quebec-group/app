package com.quebec;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;



public class EventsFeedFragment extends Fragment implements View.OnClickListener {

    private EventsFeedInteractionListener mListener;

    public EventsFeedFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
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

        /* Setup the onClick event for the event button. */

        Button b = (Button) v.findViewById(R.id.event1button);
        b.setOnClickListener(this);
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

    /**
     * React to an onClick event on the fragment.
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            /* If the event button was clicked, call the onEventSelected method in the activity in order
               to load in the EventDetailFragment.
             */
            case R.id.event1button:
                mListener.onEventSelected();
                break;
        }
    }


    public interface EventsFeedInteractionListener {
        void onEventSelected();
    }


}
