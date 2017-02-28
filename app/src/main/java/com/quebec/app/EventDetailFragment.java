package com.quebec.app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.quebec.services.APICallback;
import com.quebec.services.APIManager;
import com.quebec.services.Video;

import java.util.ArrayList;

import static com.quebec.app.EventVideoUploadSelect.EVENT_VIDEO_MODE;


public class EventDetailFragment extends Fragment implements AdapterView.OnItemClickListener,
                                                             View.OnClickListener {

    public static final String EVENT_KEY = "event_key";
    private static String LOG_TAG = EventDetailFragment.class.getSimpleName();

    private Event mEvent;

    private TextView eventNameTextView;
    private TextView eventDetailDescription;

    private View mFragmentView;
    private GridView gridView;

    private Button eventLikeButton;

    private boolean eventLikeState;
    private int eventLikes;

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

        eventLikeButton = (Button) mFragmentView.findViewById(R.id.event_detail_likes);
        eventLikeButton.setOnClickListener(this);

        /* Add event handler for the buttons. */

        Button eventLocationButton = (Button) mFragmentView.findViewById(R.id.event_detail_location_button);
        eventLocationButton.setOnClickListener(this);

        Button eventUploadButton = (Button) mFragmentView.findViewById(R.id.event_detail_uploadBtn);
        eventUploadButton.setOnClickListener(this);

        /* If the event has been initialised, then insert the Event information onto the
           the page */
        if (mEvent != null) {
            eventNameTextView.setText(mEvent.getEventName());
            eventDetailDescription.setText("");

            eventLikes = mEvent.getLikesCount();
            eventLikeButton.setText(mEvent.getLikesCount() + " likes");

            if (mEvent.getLikes()) {
                eventLikeState = true;
                eventLikeButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_heart_filled, 0, 0, 0);
            }
            else {
                eventLikeState  = false;
            }
         }


        addVideosToView();

        gridView = (GridView) mFragmentView.findViewById(R.id.eventUsers);

        /* Add the videos to the view. */
        addVideosToView();

       /* Add the users related to the event to the view. */
        addUsersToView();

        return mFragmentView;
    }


    /**
     * Add the videos related to the event to the view.
     */
    private void addVideosToView() {

        ArrayList<Video> videos = new ArrayList<Video>();
        videos.add(new Video("http://techslides.com/demos/sample-videos/small.mp4"));
        videos.add(new Video("http://clips.vorwaerts-gmbh.de/VfE_html5.mp4"));

        /* Add the videos to the view, through the use of the card view. */
        EventDetailVideoAdapterItem adapter = new EventDetailVideoAdapterItem(this.getContext(), this.getActivity(), videos);

        /* Makes use of the RecyclerView for the horizontal scrolling field. */
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext() ,LinearLayoutManager.HORIZONTAL, false);
        RecyclerView eventItemTicker = (RecyclerView) mFragmentView.findViewById(R.id.events_detail_videos_view);
        eventItemTicker.setLayoutManager(layoutManager);
        eventItemTicker.setAdapter(adapter);
    }

    /**
     * Add the users to the view.
     */
    private void addUsersToView() {

        EventUsersAdapterItem adapter = new EventUsersAdapterItem(this.getContext(), R.layout.adapter_grid_event_user, mEvent.getAttendees());
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(this);

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
     * Click event for the list elements.
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {

        Log.e("message", "On this page");
        gridView.setItemChecked(position, true);
        User u = (User) gridView.getItemAtPosition(position);

        // Call the interaction listener with the Event object.
        mListener.onEventUserSelected(u);

    }

    @Override
    public void onClick(View view) {
         switch (view.getId()) {
             case R.id.event_detail_location_button:
                 mListener.openEventDetailLocation();
                 break;
             case R.id.event_detail_uploadBtn:
                 showVideoUpload();
                 break;
             case R.id.event_detail_likes:
                 likeEventClick();
                 break;
         }
    }

    private void showVideoUpload() {
        Intent intent = new Intent(this.getContext(), EventVideoUploadSelect.class);
        intent.putExtra(EVENT_VIDEO_MODE, 1);
        startActivity(intent);
    }


    private void likeEventClick() {


        if (eventLikeState == false) {
            eventLikeButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_heart_filled, 0, 0, 0);
            eventLikes = eventLikes + 1;
            APIManager.getInstance().likeEvent(mEvent.getEventID(), new APICallback<String>() {
                @Override
                public void onSuccess(String responseBody) {
                    Log.d(LOG_TAG, "Liked event");
                }

                @Override
                public void onFailure(String message) {
                    Log.e(LOG_TAG, "Liked event failed");
                }
            });
        }
        else {
            eventLikeButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_heart_empty, 0, 0, 0);
            eventLikes = eventLikes - 1;
            APIManager.getInstance().unlikeEvent(mEvent.getEventID(), new APICallback<String>() {
                @Override
                public void onSuccess(String responseBody) {
                    Log.d(LOG_TAG, "Liked event");
                }

                @Override
                public void onFailure(String message) {
                    Log.e(LOG_TAG, "Liked event failed");
                }
            });
        }

        eventLikeButton.setText(eventLikes + " likes");
        eventLikeState = eventLikeState ? false : true;
    }


    /**
     * The interface which must be implemented by the related activity.
     */
    public interface OnEventDetailInteractionListener {
        void onEventUserSelected(User u);
        void openEventDetailLocation();
    }
}
