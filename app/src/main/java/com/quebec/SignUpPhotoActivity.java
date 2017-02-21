package com.quebec;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.VideoView;

import static com.quebec.VideoUploadActivity.REQUEST_VIDEO_CAPTURE;

public class SignUpPhotoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_photo);
    }

    /**
     * Launches the standard Android video interface for taking videos.
     * @param view is the view from which the action was called.
     * */
    public void takePhoto(View view) {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
        }
    }

    /**
     * Launches the Android gallery in order to select a video from the gallery
     * @param view
     */
    public void choosePhoto(View view) {
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
        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            Uri videoUri = intent.getData();

            VideoView videoView = (VideoView) findViewById(R.id.videoView);
            videoView.setVideoURI(videoUri);
        }
    }

}
