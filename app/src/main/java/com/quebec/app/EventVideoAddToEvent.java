package com.quebec.app;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.amazonaws.mobile.content.ContentItem;
import com.amazonaws.mobile.content.ContentProgressListener;
import com.quebec.services.APICallback;
import com.quebec.services.APIManager;
import com.quebec.services.Video;

import java.io.File;

import static com.quebec.app.EventVideoUploadPreview.EVENT_OBJECT;
import static com.quebec.app.EventVideoUploadPreview.VIDEO_URI;
import static com.quebec.app.EventVideoUploadSelect.EVENT_VIDEO;

public class EventVideoAddToEvent extends AppCompatActivity implements View.OnClickListener {

    /* Event that the video should be uploaded to. */
    private Event mEvent;

    private String mVideoURI = "";
    private String mLocation = "";

    private File video;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_video_add_to_event);

        /* Define the elements on the view. */
        final ProgressBar progressBar = (ProgressBar) this.findViewById(R.id.event_video_add_upload_progress);
        final Button saveButton = (Button) this.findViewById(R.id.event_video_add_saveBtn);

        saveButton.setOnClickListener(this);

        Intent intent = getIntent();

        String mVideoURI = intent.getStringExtra(VIDEO_URI);
        mEvent = intent.getParcelableExtra(EVENT_VIDEO);

        Uri u = Uri.parse(mVideoURI);

        VideoUploadHandler uploader = new VideoUploadHandler();
        video = new File(VideoUploadHandler.getPathFromURI(getApplicationContext(), u));

        uploader.uploadVideo(video, new ContentProgressListener() {
            @Override
            public void onSuccess(ContentItem contentItem) {
                saveButton.setEnabled(true);
            }

            @Override
            public void onProgressUpdate(String filePath, boolean isWaiting, long bytesCurrent, long bytesTotal) {
                progressBar.setMax((int) bytesTotal);
                progressBar.setProgress((int) bytesCurrent);
            }

            @Override
            public void onError(String filePath, Exception ex) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.event_video_add_saveBtn:
                uploadVideo();
        }
    }

    private void uploadVideo() {

        final String videoPath = VideoUploadHandler.getFullS3Path(video);

        APIManager.getInstance().addVideoToEvent(videoPath, mEvent, new APICallback<String>() {
            public static final String LOG_TAG = "Log";

            @Override
            public void onSuccess(String responseBody) {
                Log.d(LOG_TAG, "Video added to event");
            }

            @Override
            public void onFailure(String message) {

            }
        });

        Intent intent = new Intent(this.getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
