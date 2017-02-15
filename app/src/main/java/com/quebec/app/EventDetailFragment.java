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

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class EventDetailFragment extends Fragment implements OnMapReadyCallback {

    public static final String EVENT_KEY = "event_key";

    private String eventID;
    private Event mEvent;

    private TextView eventNameTextView;
    private TextView eventDetailDescription;

    private VideoView eventVideoview;
    private MapView eventMapView;

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
                             Bundle savedInstanceState)

    {
        // Inflate the layout for this fragment

        mFragmentView = inflater.inflate(R.layout.fragment_event_detail, container, false);
        eventNameTextView = (TextView) mFragmentView.findViewById(R.id.eventDetailName);
        eventDetailDescription = (TextView) mFragmentView.findViewById(R.id.eventDetailDescription);
        eventVideoview = (VideoView) mFragmentView.findViewById(R.id.eventVideoView);
        eventMapView = (MapView) mFragmentView.findViewById(R.id.eventMapView);

        /* If the event has been initialised, then insert the Event information onto the
           the page */
        if (mEvent != null) {
            eventNameTextView.setText(mEvent.getName());
            eventDetailDescription.setText(mEvent.getDescription());

            // TODO remove the example video.
            Uri u = Uri.parse("http://clips.vorwaerts-gmbh.de/VfE_html5.mp4");
            eventVideoview.setVideoURI(u);

            eventMapView.getMapAsync(this);

            eventVideoview.start();
        }

        gridView = (GridView) mFragmentView.findViewById(R.id.eventUsers);

        User[] values = new User[] {
                new User("Andrew Deniszczyc"),
                new User("John Smith"),
                new User("Pete Testing"),
                new User("Evian Water"),
                new User("Nokia Phone")
        };

        EventUsersAdapterItem adapter = new EventUsersAdapterItem(this.getContext(), R.layout.adapter_grid_event_user, values);
        gridView.setAdapter(adapter);

        return mFragmentView;
    }

    /**
     * Handles the map loading from the event.
     * @param map
     */
    @Override
    public void onMapReady(GoogleMap map) {
        // Needs to call MapsInitializer before doing any CameraUpdateFactory calls
        MapsInitializer.initialize(this.getActivity());

        // Updates the location and zoom of the MapView
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(43.1, -87.9), 10);
        map.animateCamera(cameraUpdate);

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

    }
}
