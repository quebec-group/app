package com.quebec.app;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
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

    /**
     * Converts from a URI to a complete file path. Handles edge cases for Android KitKat etc.
     * @param context
     * @param uri
     * @return
     */
    public static String getPathFromURI(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

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
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_upload_video_details);

        final ProgressBar progressBar = (ProgressBar) this.findViewById(R.id.event_video_upload_progress);

        Intent intent = getIntent();
        mVideoURI = intent.getStringExtra(VIDEO_URI);

        Uri u = Uri.parse(mVideoURI);
        File video = new File(getPathFromURI(this.getApplicationContext(), u));


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


        /* Added the event listener for the button click. */
        Button btn = (Button) this.findViewById(R.id.event_upload_video_saveBtn);
        btn.setOnClickListener(this);

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

