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

/**
 * Created by Andrew on 27/02/2017.
 */

public class EventDetailVideoDialog extends Dialog {
    private Activity mActivity;

    public EventDetailVideoDialog(Context context, Activity activity) {
        super(context);
        this.mActivity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.event_detail_video_popover_dialog);

        VideoView videoView = (VideoView) findViewById(R.id.event_detail_video_popover_video);

        // Get and update the textview with data from the Video.
        TextView text = (TextView) findViewById(R.id.event_detail_video_popover_text);

        MediaController mediaController = new MediaController(this.mActivity);
        mediaController.setAnchorView(videoView);

        // Add the video to the dialog box.
        Uri video = Uri.parse("http://clips.vorwaerts-gmbh.de/VfE_html5.mp4");
        videoView.setMediaController(mediaController);
        videoView.setVideoURI(video);
        videoView.setZOrderOnTop(true);
        videoView.start();

    }
}
