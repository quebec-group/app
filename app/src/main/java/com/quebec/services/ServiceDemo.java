package com.quebec.services;

import android.support.v4.app.Fragment;
import android.util.Log;

import com.amazonaws.http.HttpMethodName;
import com.amazonaws.mobile.AWSMobileClient;
import com.amazonaws.mobile.api.CloudLogicAPI;
import com.amazonaws.mobile.api.CloudLogicAPIConfiguration;
import com.amazonaws.mobile.api.CloudLogicAPIFactory;
import com.amazonaws.mobile.util.ThreadUtils;
import com.amazonaws.mobileconnectors.apigateway.ApiRequest;
import com.amazonaws.mobileconnectors.apigateway.ApiResponse;
import com.amazonaws.util.IOUtils;
import com.amazonaws.util.StringUtils;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by callum on 14/02/2017.
 */

public class ServiceDemo {
    private static String LOG_TAG = ServiceDemo.class.getSimpleName();

    private CloudLogicAPIConfiguration apiConfiguration;

    public ServiceDemo() {
        apiConfiguration = CloudLogicAPIFactory.getAPIs()[0];
    }

    public void test() {
        Log.d(LOG_TAG, "Invoke");

        final String method = "POST";
        final String path = "/api/createUser";
        final String body = "{\n" +
                "  \"name\" : \"someName\"\n" +
                "}";

        String queryStringText = "";

        final Map<String, String> parameters = new HashMap<>();

        final CloudLogicAPI client =
                AWSMobileClient.defaultMobileClient().createAPIClient(apiConfiguration.getClientClass());

        final Map<String, String> headers = new HashMap<>();

        final byte[] content = body.getBytes(StringUtils.UTF8);

        ApiRequest tmpRequest =
                new ApiRequest(client.getClass().getSimpleName())
                        .withPath(path)
                        .withHttpMethod(HttpMethodName.valueOf(method))
                        .withHeaders(headers)
                        .addHeader("Content-Type", "application/json")
                        .withParameters(parameters);

        final ApiRequest request;

        // Only set body if it has content.
        if (body.length() > 0) {
            request = tmpRequest
                    .addHeader("Content-Length", String.valueOf(content.length))
                    .withBody(content);
        } else {
            request = tmpRequest;
        }


        // Make network call on background thread
        new Thread(new Runnable() {
            Exception exception = null;

            @Override
            public void run() {
                try {
                    Log.d(LOG_TAG, "Invoking API w/ Request : " + request.getHttpMethod() + ":" + request.getPath());

                    long startTime = System.currentTimeMillis();

                    final ApiResponse response = client.execute(request);

                    final long latency = System.currentTimeMillis() - startTime;

                    final InputStream responseContentStream = response.getContent();

                    if (responseContentStream != null) {
                        final String responseData = IOUtils.toString(responseContentStream);
                        Log.d(LOG_TAG, "Response : " + responseData);
                    }

//                    ThreadUtils.runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            if (!isDetached()) {
//                                statusView.setText(response.getStatusCode() + " " + response.getStatusText());
//                                latencyView.setText(String.format("%4.3f sec", latency / 1000.0f));
//                            }
//                        }
//                    });
                } catch (final Exception exception) {
                    Log.e(LOG_TAG, exception.getMessage(), exception);
                    exception.printStackTrace();

                    ThreadUtils.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.e(LOG_TAG, exception.getMessage());
                        }
                    });
                }
            }
        }).start();
    }
}
