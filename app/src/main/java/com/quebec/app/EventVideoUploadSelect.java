package com.quebec.app;

import android.*;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;


public class EventVideoUploadSelect extends AppCompatActivity {

    public static final int REQUEST_VIDEO_CAPTURE = 1;
    public static final String EVENT_VIDEO_MODE = "event_video_mode";
    public static final String EVENT_ID = "event_id";
    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 2;
    private static final int MY_PERMISSIONS_EXTERNAL_STORAGE = 3;

    private int eventId;
    private int uploadMode = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_upload_video_select);

        /* Receive an intent to detect whether this is a new event, or
           a new video on an existing event.
         */

        Intent intent = getIntent();

        uploadMode = intent.getIntExtra(EVENT_VIDEO_MODE, 0);
        eventId = intent.getIntExtra(EVENT_ID, 0);


        if (uploadMode ==0){
            TextView description = (TextView)(this.findViewById(R.id.editText2));
            description.setText("Upload a video to create a new event and automatically tag people in the video.");
        }
        else{
            TextView description = (TextView)(this.findViewById(R.id.editText2));
            description.setText("This will add a video to the event, and tag any previously unrecognised guests of the event.");
        }
    }


    /**
     * Launches the standard Android video interface for taking videos.
     * @param view is the view from which the action was called.
     * */
    public void takeVideo(View view) {

         /* Ensure the permissions have been enabled. If not, launch the view to allow permissions to the application. */
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)  {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);
            return;
        }

        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
        }
    }

    public void takeVideoRun() {
         /* Ensure the permissions have been enabled. If not, launch the view to allow permissions to the application. */
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)  {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);
            return;
        }

        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
        }
    }

    /**
     * Launches the Android gallery in order to select a video from the gallery
     * @param view
     */
    public void chooseVideo(View view) {
        chooseVideoRun();
    }

    public void chooseVideoRun() {

          /* Ensure the permissions have been enabled. If not, launch the view to allow permissions to the application. */
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)  {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_EXTERNAL_STORAGE);
            return;
        }

        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Video"), 1);
    }


    /**
     * Return when the permissions have been accepted.
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    takeVideoRun();
                } else {
                    // TODO: Handle permissions not provided, by showing an error message or similar.
                }
                return;
            }
            case MY_PERMISSIONS_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    chooseVideoRun();
                } else {
                    // TODO: Handle permissions not provided, by showing an error message or similar.
                }
            }
        }
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

            Intent intent1 = new Intent(this, EventVideoUploadDetails.class);
            intent1.putExtra("videoUri", videoUri.toString());

            /* Pass the video upload mode to the next activity. */
            intent1.putExtra(EVENT_VIDEO_MODE, uploadMode);
            intent1.putExtra(EVENT_ID, eventId);

            startActivity(intent1);

        }
    }

}