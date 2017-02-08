package com.quebec;


import android.content.Intent;

import android.net.Uri;

import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SignUpPhotoActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_TAKE_PHOTO = 1;
    private static final int RESULT_GALLERY = 2;

    private ImageView signup_image_preview;
    private TextView error_text;

    private Uri mCropImageUri;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_photo);

        signup_image_preview = (ImageView) findViewById(R.id.signup_image_preview);
        error_text = (TextView) findViewById(R.id.signup_image_error);
    }


    /**
     * Launches the standard Android video interface for taking videos.
     * https://developer.android.com/training/camera/photobasics.html
     *
     * @param view is the view from which the action was called.
     */
    public void takePhoto(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent

        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                error_text.setText(R.string.signup_photo_error_permissions);
            }

            // If the File was created, launch the Camera activity.
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this, "com.quebec.android.fileprovider", photoFile);

                mCropImageUri = photoURI;

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }

    }


    /**
     * Copy the image from a given Uri to a File destination.
     * @param srcUri
     * @param dst
     * @throws IOException
     */
    public void copy(Uri srcUri, File dst) throws IOException {
        InputStream in = getContentResolver().openInputStream(srcUri);
        OutputStream out = new FileOutputStream(dst);

        // Transfer bytes from in to out
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }

    /**
     * Creates a temporary image file for use with the image cropper.
     * https://developer.android.com/training/camera/photobasics.html
     *
     * @return
     * @throws IOException
     */
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        return image;

    }

    /**
     * Launches the Android gallery in order to select a video from the gallery
     *
     * @param view
     */
    public void choosePhoto(View view) {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, RESULT_GALLERY);

    }

    /**
     * Handle the image selection from the gallery. The image must be copied to temp
     * storage so that it can be cropped on the device to the required dimensions.
     * @param selectedImage
     */
    public void handleImageSelectionGallery(Uri selectedImage) {
        File photoFile = null;

        try {
            photoFile = createImageFile();
        } catch (IOException ex) {
            error_text.setText(R.string.signup_photo_error_permissions);
        }
        // Continue only if the File was successfully created
        if (photoFile != null) {
            Uri photoURI = FileProvider.getUriForFile(this, "com.quebec.android.fileprovider", photoFile);
            mCropImageUri = photoURI;
            try {
                copy(selectedImage, photoFile);
                launchCroppingTool();

            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }

    /**
     * Launch the cropping tool. Any cropping restrictions such as
     * only allowing square images should be included here.
     */
    public void launchCroppingTool() {
        CropImage.activity(mCropImageUri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(this);
    }

    /**
     * Callback from the video selection from taking a video or choosing a video.
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            launchCroppingTool();
        } else if (requestCode == RESULT_GALLERY && resultCode == RESULT_OK && null != data) {
            handleImageSelectionGallery(data.getData());
        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            // Image has been successfully cropepd, now we need to handle it by uploading the photo to the server.
            // TODO image completed
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();

                signup_image_preview.setImageURI(resultUri);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                error_text.setText(R.string.signup_photo_error_cropping);
            }

        }


    }


}
