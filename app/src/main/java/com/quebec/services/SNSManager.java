package com.quebec.services;

import com.amazonaws.mobile.AWSMobileClient;
import com.amazonaws.mobile.push.PushManager;

/**
 * Created by callum on 25/02/2017.
 */

public class SNSManager {
    private final PushManager pushManager;

    public SNSManager() {
        pushManager = AWSMobileClient.defaultMobileClient()
                .getPushManager();
    }

    public String getArn() {
        pushManager.registerDevice();
        pushManager.setPushEnabled(true);
        pushManager.subscribeToTopic(pushManager.getDefaultTopic());

        return pushManager.getEndpointArn();
    }
}
