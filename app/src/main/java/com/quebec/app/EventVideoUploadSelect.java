package com.quebec.app;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class EventVideoUploadSelect extends AppCompatActivity {

    public static final int REQUEST_VIDEO_CAPTURE = 1;
    public static final String EVENT_VIDEO = "event_video";
    public static final String EVENT_VIDEO_MODE = "event_video_mode";

    private Event event;

    private int uploadMode = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_upload_video_select);

        /* Receive an intent to detect whether this is a new event, or
           a new video on an existing event.
         */

        Intent intent = getIntent();

        Bundle b = intent.getExtras();

        if (b != null) {
            if (b.containsKey(EVENT_VIDEO_MODE)) {
                uploadMode = intent.getIntExtra(EVENT_VIDEO_MODE, 0);
            }

            if (b.containsKey(EVENT_VIDEO)) {
                event = intent.getExtras().getParcelable(EVENT_VIDEO);
            }
        }
    }


    /**
     * Launches the standard Android video interface for taking videos.
     * @param view is the view from which the action was called.
     * */
    public void takeVideo(View view) {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /**
     * Launches the Android gallery in order to select a video from the gallery
     * @param view
     */
    public void chooseVideo(View view) {
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Video"), 1);

    }

    /**
     * Callback from the video selection from taking a video or choosing a video.
     * @param requestCode
     * @param resultCode
     * @param intent
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (resultCode == RESULT_OK) {
            Uri videoUri = intent.getData();

            Intent intent1 = new Intent(this, EventVideoUploadPreview.class);
            intent1.putExtra("videoUri", videoUri.toString());

            /* Pass the video upload mode to the next activity. */
            intent1.putExtra(EVENT_VIDEO_MODE, uploadMode);
            intent1.putExtra(EVENT_VIDEO, event);

            startActivity(intent1);



        }

    }


}
