package com.quebec.app;


import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.mobile.AWSMobileClient;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSAsyncClient;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.SendMessageRequest;

public class Monitor {
    private static String MONITOR_QUEUE = "https://sqs.eu-west-1.amazonaws.com/926867918335/monitoring";
    private AmazonSQSAsyncClient sqs;

    private static Monitor instance;

    public static Monitor getInstance() {
        if (instance == null) {
            instance = new Monitor();
        }
        return instance;
    }

    private Monitor() {
        sqs = new AmazonSQSAsyncClient(AWSMobileClient.defaultMobileClient().getIdentityManager().getCredentialsProvider().getCredentials());
    }

    public void s3Call(String data) {
        SendMessageRequest request = new SendMessageRequest(MONITOR_QUEUE, "s3:"+data);
        sqs.sendMessageAsync(request);
    }

    public void apiCall(String data) {
        SendMessageRequest request = new SendMessageRequest(MONITOR_QUEUE, "api:"+data);
        sqs.sendMessageAsync(request);
    }

}
