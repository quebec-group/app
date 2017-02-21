package com.quebec.app;


import android.Manifest;
import android.content.Intent;

import android.content.pm.PackageManager;
import android.net.Uri;

import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amazonaws.mobile.content.ContentItem;
import com.amazonaws.mobile.content.ContentProgressListener;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Andrew on 09/02/2017.
 */

public class ProfilePictureActivity extends AppCompatActivity {


    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_TAKE_PHOTO = 1;
    private static final int RESULT_GALLERY = 2;
    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 3;
    private static final int MY_PERMISSIONS_EXTERNAL_STORAGE = 4;

    private ImageView signup_image_preview;
    private TextView errorText;
    private Button confirmButton;
    private LinearLayout resultView;

    private Uri mCropImageUri;
    private Uri croppedImageUri;

    public ProfilePictureActivity() {
        super();
    }


    public ProfilePictureActivity(ImageView signup_image_preview, TextView errorText, Button confirmButton) {
        this.signup_image_preview = signup_image_preview;
        this.errorText = errorText;
        this.confirmButton = confirmButton;
    }

    public void setupElements(ImageView signup_image_preview, TextView errorText, Button confirmButton, LinearLayout resultView) {
        this.signup_image_preview = signup_image_preview;
        this.errorText = errorText;
        this.confirmButton = confirmButton;
        this.resultView = resultView;
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

    public void launchCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent

        /* Ensure the permissions have been enabled. If not, launch the view to allow permissions to the application. */
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)  {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);
            return;
        }

        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                errorText.setText(R.string.signup_photo_error_permissions);
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
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);

        return image;

    }


    /**
     * Launches the Android gallery in order to select a video from the gallery
     */
    public void choosePhoto() {

        /* Ensure the permissions have been enabled. If not, launch the view to allow permissions to the application. */
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)  {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_EXTERNAL_STORAGE);
            return;
        }

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
            errorText.setText(R.string.signup_photo_error_permissions);
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
                .setCropShape(CropImageView.CropShape.RECTANGLE)
                .setFixAspectRatio(true)
                .start(this);
    }

    /**
     * Return when the permissions have been accepted.
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    launchCamera();
                } else {
                    // TODO: Handle permissions not provided, by showing an error message or similar.
                }
                return;
            }
            case MY_PERMISSIONS_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    choosePhoto();
                } else {
                    // TODO: Handle permissions not provided, by showing an error message or similar.
                }
            }
        }
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

            // Image has been successfully cropped, now we need to handle it by uploading the photo to the server.
            // TODO image completed

            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                croppedImageUri = result.getUri();
                signup_image_preview.setImageURI(croppedImageUri);
                resultView.setVisibility(View.VISIBLE);
                confirmButton.setEnabled(true);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                errorText.setText(R.string.signup_photo_error_cropping);
            }

        }


    }

    public void showMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void confirmPhoto() {
        ProfilePictureHandler up = new ProfilePictureHandler();

        up.uploadProfilePicture(croppedImageUri.getPath(), new ContentProgressListener() {
            @Override
            public void onSuccess(ContentItem contentItem) {
                showMainActivity();
            }

            @Override
            public void onProgressUpdate(String filePath, boolean isWaiting, long bytesCurrent, long bytesTotal) {
                // TODO
            }

            @Override
            public void onError(String filePath, Exception ex) {
                // TODO
                Log.e("error", "an error occurred");
            }
        });
    }
}
