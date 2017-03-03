package com.quebec.app;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.Window;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.quebec.services.Video;

/**
 * Created by Andrew on 27/02/2017.
 */

public class EventDetailVideoDialog extends Dialog {
    private Activity mActivity;
    private Video mVideo;

    public EventDetailVideoDialog(Video video, Context context, Activity activity) {
        super(context);
        this.mActivity = activity;
        this.mVideo = video;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.event_detail_video_popover_dialog);

        VideoView videoView = (VideoView) findViewById(R.id.event_detail_video_popover_video);
        
        MediaController mediaController = new MediaController(this.mActivity);
        mediaController.setAnchorView(videoView);

        // Add the video to the dialog box.
        Uri video = Uri.parse(S3Handler.getInstance().getVideoURL(mVideo.getVideoPath()));
        videoView.setMediaController(mediaController);
        videoView.setVideoURI(video);
        videoView.setZOrderOnTop(true);
        videoView.start();


    }
}
