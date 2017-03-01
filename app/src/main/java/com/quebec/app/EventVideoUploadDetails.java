package com.quebec.app;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.amazonaws.mobile.content.ContentItem;
import com.amazonaws.mobile.content.ContentProgressListener;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.quebec.services.APICallback;
import com.quebec.services.APIManager;

import java.io.File;


public class EventVideoUploadDetails extends AppCompatActivity implements View.OnClickListener {
    private static String LOG_TAG = EventVideoUploadDetails.class.getSimpleName();

    static final String VIDEO_URI = "videoUri";

    private String mVideoURI;
    private EditText eventTitleEditText;
    private Button saveButton;
    private String videoPath = "";
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    private File video;

    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }


    /**
     * onCreate loads when the video upload panel is loaded.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_upload_video_details);

        eventTitleEditText = (EditText) findViewById(R.id.event_upload_details_event_name);
        saveButton = (Button) findViewById(R.id.event_upload_video_saveBtn);

        final ProgressBar progressBar = (ProgressBar) this.findViewById(R.id.event_video_upload_progress);

        Intent intent = getIntent();
        mVideoURI = intent.getStringExtra(VIDEO_URI);

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


        /* Added the event listener for the button click. */
        Button btn = (Button) this.findViewById(R.id.event_upload_video_saveBtn);
        btn.setOnClickListener(this);

        saveButton.setEnabled(false);

        saveButton.setOnClickListener(this);

        Log.e("tag", mVideoURI);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


    @Override
    public void onClick(View v) {
        if (v.equals(saveButton)) {
            //TODO fill location
            final String location = "TODO";
            final String videoPath = VideoUploadHandler.getFullS3Path(video);

            APIManager.getInstance().createEvent(
                    eventTitleEditText.getText().toString(),
                    location,
                    videoPath,
                    new APICallback<String>() {
                        @Override
                        public void onSuccess(String responseBody) {
                            Log.d(LOG_TAG, "Event created");
                        }

                        @Override
                        public void onFailure(String message) {
                            Log.e(LOG_TAG, "Event creation failed");
                        }
                    });


            Intent intent = new Intent(this.getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("EventVideoUploadDetails Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}

