package com.quebec.app;

import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ProfilePictureSignUpActivity extends ProfilePictureActivity {

    public ProfilePictureSignUpActivity() {
        super();
    }

    public ProfilePictureSignUpActivity(ImageView signup_image_preview,
                                        TextView errorText,
                                        Button confirmButton) {
        super(signup_image_preview, errorText, confirmButton);
    }


    /**
     * onCreate sets up the elements for use within the class.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_photo);

        ImageView signup_image_preview = (ImageView) findViewById(R.id.signup_image_preview);
        TextView errorText = (TextView) findViewById(R.id.signup_image_error);
        Button confirmButton = (Button) findViewById(R.id.signup_photo_confirm);

        super.setupElements(signup_image_preview, errorText, confirmButton);

    }

    /**
     * onClick event for the choosePhoto button
     * @param view
     */
    public void choosePhoto(View view) {
        super.choosePhoto(view);
    }

    /**
     * onClick event for the takePhoto button
     * @param view
     */
    public void takePhoto(View view) {
        super.launchCamera();
    }

    public void confirmPhoto(View view) {
        super.confirmPhoto();
    }




}
