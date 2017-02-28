package com.quebec.app;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.VideoView;

public class SignUpVideoActivity extends AppCompatActivity {

    public static final String EVENT_VIDEO = "event_video";


    private Event event;
    private int uploadMode = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_video);
    }


    /**
     * Launches the standard Android video interface for taking videos.
     * @param view is the view from which the action was called.
     * */
    public void takeVideo(View view) {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takeVideoIntent, 0);
        }
    }


    /**
     * Callback from taking a video.
     * @param requestCode
     * @param resultCode
     * @param intent
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 0 && resultCode == RESULT_OK) {
            Uri videoUri = intent.getData();

            Intent intent1 = new Intent(this, MainActivity.class);
            intent1.putExtra("videoUri", videoUri.toString());

            intent1.putExtra(EVENT_VIDEO, event);

            startActivity(intent1);

        }
    }

}
