package com.quebec.app;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;

public class AddedVideoPreviewActivity extends AppCompatActivity {

    static final String VIDEO_URI = "videoUri";
    static final int REQUEST_VIDEO_CAPTURE = 1;
    private VideoView mVideoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_added_video_preview);

        Intent intent = getIntent();
        Uri videoURI = Uri.parse(intent.getStringExtra(VIDEO_URI));

        mVideoView = (VideoView)findViewById(R.id.videoView);
        mVideoView.setVideoURI(videoURI);
        /* Add scrubbing controls to the video view. */
        MediaController ctrl = new MediaController(this);
        mVideoView.setMediaController(ctrl);
        mVideoView.start();

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

    /**
     * Retrieves the video that has been recorded and displays it in a VideoView
     * @param requestCode
     * @param resultCode
     * @param intent
     */

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {

    }

    /**
     * Launches the Android gallery in order to select a video from the gallery
     * @param view
     */
    public void chooseVideo(View view) {
        Intent intent = new Intent(this, AddVideoToEventActivity.class);
        startActivity(intent);
        //Intent intent = new Intent();
        //intent.setType("video/*");
        //intent.setAction(Intent.ACTION_GET_CONTENT);
        //startActivityForResult(Intent.createChooser(intent,"Select Video"), 1);
        //showVideoUploadActivity();

    }

    public void uploadVideo(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


    /**
     * Callback from the video selection from taking a video or choosing a video.
     * @param requestCode
     * @param resultCode
     * @param intent
     */
    // @Override
    /*protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            Uri videoUri = intent.getData();

            VideoView videoView = (VideoView) findViewById(R.id.videoPreview);
            videoView.setVideoURI(videoUri);
        }
    }*/


}

