package com.quebec.app;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.amazonaws.mobile.content.ContentItem;
import com.amazonaws.mobile.content.ContentProgressListener;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.quebec.services.APICallback;
import com.quebec.services.APIManager;

import java.io.File;

import static com.quebec.app.EventVideoUploadSelect.EVENT_ID;


public class EventVideoUploadDetails extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {

    private GoogleApiClient mGoogleApiClient;

    private static String LOG_TAG = EventVideoUploadDetails.class.getSimpleName();

    static final String VIDEO_URI = "videoUri";

    private String mVideoURI = "";
    private String mLocation = "";

    private EditText eventTitleEditText;
    private EditText eventLocationEditText;

    private Button saveButton;
    private String videoPath = "";
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    private File video;
    private TextView locationTextView;

    private boolean mLocationPermissionGranted;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 247;
    private Location mLastKnownLocation;

    private int eventID;
    private int uploadMode;



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
        locationTextView = (TextView) findViewById(R.id.locationTextView);

        final ProgressBar progressBar = (ProgressBar) this.findViewById(R.id.event_video_upload_progress);

        Intent intent = getIntent();
        mVideoURI = intent.getStringExtra(VIDEO_URI);

        uploadMode = intent.getIntExtra(EventVideoUploadSelect.EVENT_VIDEO_MODE, 0);

        if (uploadMode == 1) {
            eventID = intent.getIntExtra(EVENT_ID, -1);
            eventTitleEditText.setVisibility(View.INVISIBLE);
            findViewById(R.id.event_upload_details_event_name_text).setVisibility(View.INVISIBLE);
            saveButton.setVisibility(View.INVISIBLE);
        }

        Uri u = Uri.parse(mVideoURI);

        VideoUploadHandler uploader = new VideoUploadHandler();
        video = new File(VideoUploadHandler.getPathFromURI(getApplicationContext(), u));

        uploader.uploadVideo(video, new ContentProgressListener() {
            @Override
            public void onSuccess(ContentItem contentItem) {
                saveButton.setEnabled(true);
                saveButton.setAlpha(1.0f);
                if (uploadMode == 1) {
                    addVideoToEvent();
                }
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


        /* Setup the location handler to get the current user location. */
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */,
                        this /* OnConnectionFailedListener */)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .build();

        mGoogleApiClient.connect();

        /* Added the event listener for the button click. */
        Button btn = (Button) this.findViewById(R.id.event_upload_video_saveBtn);
        btn.setOnClickListener(this);

        saveButton.setEnabled(false);
        saveButton.setAlpha(0.5f);

        saveButton.setOnClickListener(this);


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void addVideoToEvent() {
        final String videoPath = VideoUploadHandler.getFullS3Path(video);

        APIManager.getInstance().addVideoToEvent(videoPath, eventID, new APICallback<String>() {
            @Override
            public void onSuccess(String responseBody) {
                Log.d(LOG_TAG, "Added video to event");
            }

            @Override
            public void onFailure(String message) {
                Log.d(LOG_TAG, "Failed to add video to event: " + message);
            }
        });

        goBackToMain();
    }

    private void getDeviceLocation() {
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }

        LocationRequest mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)
                .setFastestInterval(1 * 1000);

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                mLastKnownLocation = location;
                performDeviceLocationGet();
            }
        });

    }

    private void performDeviceLocationGet() {

        if (mLocationPermissionGranted) {
            if (mLastKnownLocation != null) {
                locationTextView.setText("Location: (" + mLastKnownLocation.getLatitude() + "," + mLastKnownLocation.getLongitude() + ")");
                mLocation = Double.toString(mLastKnownLocation.getLatitude()) + "," + Double.toString(mLastKnownLocation.getLongitude());
            } else {
                // Create the LocationRequest object request
                locationTextView.setText("Location: (" + mLastKnownLocation.getLatitude() + "," + mLastKnownLocation.getLongitude() + ")");
                mLocation = "";
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
        getDeviceLocation();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.event_upload_video_saveBtn) {

            final String location = mLocation;
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
            goBackToMain();
        }
    }

    private void goBackToMain() {
        Intent intent = new Intent(this.getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
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


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        else {
            getDeviceLocation();
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }
}

