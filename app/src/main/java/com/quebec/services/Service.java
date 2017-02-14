package com.quebec.services;

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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Andy on 14/02/2017.
 */

public class Service {

    private APIRequest apiRequest;
    private ArrayList<String> responseBody = new ArrayList<>();


    public String getResponseBody() {
        return (responseBody.size() > 0) ? responseBody.get(0) : "";
    }

    public Service(APIRequest apiRequest) {
        this.apiRequest = apiRequest;
        apiConfiguration = CloudLogicAPIFactory.getAPIs()[0];
    }
    private static String LOG_TAG = Service.class.getSimpleName();

    private CloudLogicAPIConfiguration apiConfiguration;



    public void test() {

        Log.d(LOG_TAG, "Invoke");

        final String method = apiRequest.getApiEndpoint().getMethod();
        final String path = apiRequest.getApiEndpoint().getPath();

        final String body = "{\n" +
                "  \"name\" : \"andy\"\n" +
                "  \"email\" : \"email\"\n" +
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
                    Log.d(LOG_TAG, path);
                    Log.d(LOG_TAG, "Invoking API w/ Request : " + request.getHttpMethod() + ":" + request.getPath());

                    long startTime = System.currentTimeMillis();

                    final ApiResponse response = client.execute(request);

                    final long latency = System.currentTimeMillis() - startTime;

                    final InputStream responseContentStream = response.getContent();

                    if (responseContentStream != null) {
                        final String responseData = IOUtils.toString(responseContentStream);
                        Log.d(LOG_TAG, "Response : " + responseData);
                        responseBody.add(responseData);
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
