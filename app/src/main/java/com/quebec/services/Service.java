package com.quebec.services;

import android.os.AsyncTask;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Andy on 14/02/2017.
 */

public class Service extends AsyncTask<Void, Integer, APIResponse> {

    private APIRequest apiRequest;
    private ServiceCallBack callBack;
    private static String LOG_TAG = Service.class.getSimpleName();
    private CloudLogicAPIConfiguration apiConfiguration;


    public Service(APIRequest apiRequest, ServiceCallBack serviceCallBack) {
        this.apiRequest = apiRequest;
        this.callBack = serviceCallBack;
        apiConfiguration = CloudLogicAPIFactory.getAPIs()[0];
    }


    @Override
    protected APIResponse doInBackground(Void... params) {
        APIResponse apiResponse = null;
        Log.d(LOG_TAG, "Invoke");

        final String method = apiRequest.getApiEndpoint().getMethod();
        final String path = apiRequest.getApiEndpoint().getPath();
        final String body = apiRequest.getBody();

        Log.d(LOG_TAG, "\tMethod: "+ method);
        Log.d(LOG_TAG, "\tPath: "+ path);
        Log.d(LOG_TAG, "\tBody: "+ body);

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

                try {
                    Log.d(LOG_TAG, path);
                    Log.d(LOG_TAG, "Invoking API w/ Request : " + request.getHttpMethod() + ":" + request.getPath());


                    final ApiResponse response = client.execute(request);

                    final InputStream responseContentStream = response.getContent();
                    final int responseCode = response.getStatusCode();


                    // Given valid server response
                    if (responseContentStream != null) {

                        final String responseData = IOUtils.toString(responseContentStream);

                        JSONObject responseJSON = new JSONObject(responseData);
                        BaseDAO baseDAO = new BaseDAO(responseJSON);


                        apiResponse = new APIResponse(responseCode + "");
                        apiResponse.setResponseBody(baseDAO);
                        Log.d(LOG_TAG, "APIResponse code : " + apiResponse.getStatus());
                        if (apiResponse.getStatus().equals("400")) {
                            Log.d(LOG_TAG, "APIResponse code : " + responseJSON.getString("errorMessage"));
                        }

                    } else { // failed to receive response from server

                        apiResponse = new APIResponse(responseCode + "");

                    }


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

            return apiResponse;
    }


    /**
     * run post-execution of the request
     * @param apiResponse
     */
    @Override
    public void onPostExecute(APIResponse apiResponse) {
        try {
            callBack.onResponseReceived(apiResponse);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static interface ServiceCallBack {
        public void onResponseReceived(APIResponse<BaseDAO> apiResponse) throws JSONException;
    }
}
