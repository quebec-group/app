package com.quebec.app;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
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

        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);

        setContentView(R.layout.event_detail_video_popover_dialog);

        final VideoView videoView = (VideoView) findViewById(R.id.event_detail_video_popover_video);

        MediaController mediaController = new MediaController(this.mActivity);
        mediaController.setAnchorView(videoView);
        mediaController.setMediaPlayer(videoView);

        // Add the video to the dialog box.
        Uri video = Uri.parse(S3Handler.getInstance().getVideoURL(mVideo.getVideoPath()));
        videoView.setMediaController(mediaController);

        videoView.requestFocus();
        videoView.setVideoURI(video);
        videoView.setZOrderOnTop(true);

        videoView.start();
        mediaController.show();

        DisplayMetrics displayMetrics = new DisplayMetrics();
        this.mActivity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        final int windowHeight = displayMetrics.heightPixels;
        final int windowWidth = displayMetrics.widthPixels;

        final EventDetailVideoDialog window = this;
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                videoView.start();



                mp.setLooping(true);


                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(window.getWindow().getAttributes());

                //This makes the dialog take up the full width
                lp.height = (int) (mp.getVideoHeight());

                window.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);

            }
        });

    }
}
