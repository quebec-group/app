package com.quebec.app;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.VideoView;

import static com.quebec.app.EventVideoUploadSelect.EVENT_VIDEO;
import static com.quebec.app.EventVideoUploadSelect.EVENT_VIDEO_MODE;

public class EventVideoUploadPreview extends AppCompatActivity implements View.OnClickListener {

    static final String VIDEO_URI = "videoUri";
    private VideoView mVideoView;
    private String videoString;

    private int uploadMode = 0;
    private Event event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_upload_video_preview);

         /* Receive an intent to detect whether this is a new event, or
           a new video on an existing event.
         */

        Intent intent = getIntent();

        uploadMode = intent.getIntExtra(EVENT_VIDEO_MODE, 0);
        event = intent.getExtras().getParcelable(EVENT_VIDEO);

        Button useVideoButton = (Button) this.findViewById(R.id.event_video_upload_preview_uploadBtn);
        Button reuploadVideoButton = (Button) this.findViewById(R.id.event_video_upload_preview_retakeBtn);

        useVideoButton.setOnClickListener(this);
        reuploadVideoButton.setOnClickListener(this);

        videoString = intent.getStringExtra(VIDEO_URI);
        Uri videoURI = Uri.parse(videoString);

        mVideoView = (VideoView)findViewById(R.id.videoView);
        mVideoView.setVideoURI(videoURI);

        /* Add scrubbing controls to the video view. */
        MediaController ctrl = new MediaController(this);
        mVideoView.setMediaController(ctrl);
        mVideoView.start();

    }

    private void useVideo() {

        /* This is where it is determined whether the information needs to be added for
           a new event or if the video is going to be uploaded to an existing event.
         */

        if (uploadMode == 0) {
            /* New event, therefore launch the event upload activity. */
            Intent intent1 = new Intent(this.getApplicationContext(), EventVideoUploadDetails.class);
            intent1.putExtra("videoUri", videoString);
            startActivity(intent1);
        }
        else {
            /* Uploading to an existing event, therefore launch the add video to existing event activity. */
            Intent intent1 = new Intent(this.getApplicationContext(), MainActivity.class);
            startActivity(intent1);
        }
    }

    private void retakeVideo() {
        Intent intent = new Intent(this.getApplicationContext(), EventVideoUploadSelect.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.event_video_upload_preview_uploadBtn:
                useVideo();
                break;
            case R.id.event_video_upload_preview_retakeBtn:
                retakeVideo();
        }
    }
}

