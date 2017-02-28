package com.quebec.services;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;

/**
 * Created by callum on 28/02/2017.
 */

public class AWSWrapper {
    private static String LOG_TAG = AWSWrapper.class.getSimpleName();
    private static String cognitoID;
    private static CognitoCachingCredentialsProvider credentialsProvider;

    public static String getCognitoID() {
        if (cognitoID != null) {
            return cognitoID;
        }

        if (credentialsProvider != null) {
            setCognitoID(credentialsProvider.getIdentityId());
        }

        return cognitoID;
    }

    public static void setCognitoID(String cognitoID) {
        AWSWrapper.cognitoID = cognitoID;
    }

    public static void setCredentialsProvider(CognitoCachingCredentialsProvider credentialsProvider) {
        AWSWrapper.credentialsProvider = credentialsProvider;
    }
}
