package com.quebec.app;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.amazonaws.mobile.content.ContentItem;
import com.amazonaws.mobile.content.ContentProgressListener;
import com.quebec.services.APICallback;
import com.quebec.services.APIManager;

import java.io.File;

public class SignUpVideoActivity extends AppCompatActivity {
    private static String LOG_TAG = SignUpVideoActivity.class.getSimpleName();

    public static final String EVENT_VIDEO = "event_video";


    private Event event;
    private int uploadMode = 0;
    private ProgressBar progressBar;
    private TextView progressText;
    private Button takeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_video);

        progressText = (TextView) findViewById(R.id.sign_up_video_uploading_progress_Text);
        progressBar = (ProgressBar) findViewById(R.id.sign_up_video_upload_progressbar);
        takeButton = (Button) findViewById(R.id.sign_up_takeVideo);
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
            final Uri videoUri = intent.getData();

            takeButton.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
            progressText.setVisibility(View.VISIBLE);

            VideoUploadHandler uploader = new VideoUploadHandler();
            final File video = new File(VideoUploadHandler.getPathFromURI(getApplicationContext(), videoUri));

            uploader.uploadVideo(video, new ContentProgressListener() {
                @Override
                public void onSuccess(ContentItem contentItem) {
                    APIManager.getInstance().setTrainingVideo(VideoUploadHandler.getFullS3Path(video),
                            new APICallback<String>() {
                                @Override
                                public void onSuccess(String responseBody) {
                                    Log.d(LOG_TAG, "Uploaded training video successfully");
                                }

                                @Override
                                public void onFailure(String message) {
                                    Log.e(LOG_TAG, "Failed to upload training video to db");
                                }
                            });

                    final Intent intent1 = new Intent(SignUpVideoActivity.this, MainActivity.class);
                    intent1.putExtra("videoUri", videoUri.toString());
                    intent1.putExtra(EVENT_VIDEO, event);
                    startActivity(intent1);
                }

                @Override
                public void onProgressUpdate(String filePath, boolean isWaiting, long bytesCurrent, long bytesTotal) {
                    progressBar.setMax((int) bytesTotal);
                    progressBar.setProgress((int) bytesCurrent);
                }

                @Override
                public void onError(String filePath, Exception ex) {
                    Log.e(LOG_TAG, "Failed to upload training video to s3");
                }
            });

        }
    }

}
