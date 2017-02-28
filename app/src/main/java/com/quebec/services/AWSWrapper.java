package com.quebec.services;

/**
 * Created by callum on 28/02/2017.
 */

public class AWSWrapper {
    private static String cognitoID;

    public static String getCognitoID() {
        return cognitoID;
    }

    public static void setCognitoID(String cognitoID) {
        AWSWrapper.cognitoID = cognitoID;
    }
}
