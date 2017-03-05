package com.quebec.app;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.mobile.content.ContentItem;
import com.amazonaws.mobile.content.ContentProgressListener;
import com.quebec.services.APICallback;
import com.quebec.services.APIManager;

import java.io.File;

public class SignUpVideoActivity extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 1;
    private static String LOG_TAG = SignUpVideoActivity.class.getSimpleName();

    public static final String EVENT_VIDEO = "event_video";

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    /**
     * Launches the standard Android video interface for taking videos.
     * @param view is the view from which the action was called.
     * */
    public void takeVideo(View view) {
        takeVideoRun();
    }

    public void takeVideoRun() {
        /* Ensure the permissions have been enabled. If not, launch the view to allow permissions to the application. */
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)  {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);
            return;
        }

        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takeVideoIntent, 0);
        }
    }

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

                    final Intent intent = new Intent(SignUpVideoActivity.this, MainActivity.class);
                    startActivity(intent);
                }

                @Override
                public void onProgressUpdate(String filePath, boolean isWaiting, long bytesCurrent, long bytesTotal) {
                    progressBar.setMax((int) bytesTotal);
                    progressBar.setProgress((int) bytesCurrent);
                }

                @Override
                public void onError(String filePath, Exception ex) {
                    Toast.makeText(getApplicationContext(), "Failed to upload your video!", Toast.LENGTH_LONG).show();
                    Log.e(LOG_TAG, "Failed to upload training video to s3");
                }
            });

        }
    }

}
