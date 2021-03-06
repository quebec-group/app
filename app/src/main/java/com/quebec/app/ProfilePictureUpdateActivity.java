package com.quebec.app;

import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ProfilePictureUpdateActivity extends ProfilePictureActivity {

    public ProfilePictureUpdateActivity() {
        super();
    }

    public ProfilePictureUpdateActivity(ImageView signup_image_preview,
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
        setContentView(R.layout.activity_update_profile_photo);

        ImageView signup_image_preview = (ImageView) findViewById(R.id.signup_image_preview);
        TextView errorText = (TextView) findViewById(R.id.signup_image_error);
        Button confirmButton = (Button) findViewById(R.id.signup_photo_confirm);
        LinearLayout resultView = (LinearLayout) findViewById(R.id.signup_image_result);

        super.setupElements(signup_image_preview, errorText, confirmButton, resultView);

    }

    /**
     * onClick event for the choosePhoto button
     * @param view
     */
    public void choosePhoto(View view) {
        super.choosePhoto();
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
