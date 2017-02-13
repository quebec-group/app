package com.quebec.app;

import android.os.Bundle;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ProfilePictureSignUpActivity extends ProfilePictureActivity {

    public ProfilePictureSignUpActivity() {
        super();
    }

    public ProfilePictureSignUpActivity(ImageView signup_image_preview, TextView errorText) {
        super(signup_image_preview, errorText);
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

        super.setupElements(signup_image_preview, errorText);

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




}
