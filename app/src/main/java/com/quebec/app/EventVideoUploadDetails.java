package com.quebec.app;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.VideoView;

import com.amazonaws.mobile.content.ContentItem;
import com.amazonaws.mobile.content.ContentProgressListener;

import java.io.File;
import java.net.URI;

public class EventVideoUploadDetails extends AppCompatActivity implements View.OnClickListener {

    static final String VIDEO_URI = "videoUri";

    private String mVideoURI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_upload_video_details);

        final ProgressBar progressBar = (ProgressBar) this.findViewById(R.id.event_video_upload_progress);

        Intent intent = getIntent();
        mVideoURI = intent.getStringExtra(VIDEO_URI);

        Uri u = Uri.parse(mVideoURI);
        File video = new File(u.getPath());

        Button btn = (Button) this.findViewById(R.id.event_upload_video_saveBtn);
        btn.setOnClickListener(this);

        /*
        VideoUploadHandler uploader = new VideoUploadHandler();
        uploader.uploadVideo(video, new ContentProgressListener() {
            @Override
            public void onSuccess(ContentItem contentItem) {

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
        */
        Log.e("tag", mVideoURI);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.event_upload_video_saveBtn:
                Intent intent = new Intent(this.getApplicationContext(), MainActivity.class);
                startActivity(intent);
                break;
        }
    }

}

