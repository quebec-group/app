//
// Copyright 2017 Amazon.com, Inc. or its affiliates (Amazon). All Rights Reserved.
//
// Code generated by AWS Mobile Hub. Amazon gives unlimited permission to 
// copy, distribute and modify it.
//
// Source code generated from template: aws-my-sample-app-android v0.14
//
package com.quebec.app.auth;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.amazonaws.mobile.user.signin.CognitoUserPoolsSignInProvider;
import com.quebec.app.R;
import com.quebec.app.util.ViewHelper;

/**
 * Activity to prompt for sign-up confirmation information.
 */
public class SignUpConfirmActivity extends Activity {
    /** Log tag. */
    private static final String LOG_TAG = SignUpConfirmActivity.class.getSimpleName();

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_confirm);
    }

    @Override
    protected void onResume() {
        super.onResume();

        EditText userNameField = (EditText) this.findViewById(R.id.confirm_account_username);
        userNameField.setText(
                getSharedPreferences("SignUp", Context.MODE_PRIVATE).getString("username", ""));
        
    }

    /**
     * Retrieve input and return to caller.
     * @param view the Android View
     */
    public void confirmAccount(final View view) {
        final String username =
                ViewHelper.getStringValue(this, R.id.confirm_account_username);
        final String verificationCode =
                ViewHelper.getStringValue(this, R.id.confirm_account_confirmation_code);

        final Intent intent = new Intent();
        intent.putExtra(CognitoUserPoolsSignInProvider.AttributeKeys.USERNAME, username);
        intent.putExtra(CognitoUserPoolsSignInProvider.AttributeKeys.VERIFICATION_CODE, verificationCode);

        setResult(RESULT_OK, intent);

        finish();
    }
}
