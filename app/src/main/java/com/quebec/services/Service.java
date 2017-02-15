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
import java.io.UTFDataFormatException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Andy on 14/02/2017.
 */

public class Service extends AsyncTask<Void, Integer, APIResponse> {

    private APIRequest apiRequest;
    private ServiceCallBack callBack;




    public Service(APIRequest apiRequest, ServiceCallBack serviceCallBack) {
        this.apiRequest = apiRequest;
        this.callBack = serviceCallBack;
        apiConfiguration = CloudLogicAPIFactory.getAPIs()[0];
    }
    private static String LOG_TAG = Service.class.getSimpleName();

    private CloudLogicAPIConfiguration apiConfiguration;





    @Override
    protected APIResponse doInBackground(Void... params) {
        JSONObject jsonObject = new JSONObject();
        BaseDAO baseDAO = new BaseDAO(jsonObject);
        APIResponse apiResponse = null;
        Log.d(LOG_TAG, "Invoke");

        final String method = apiRequest.getApiEndpoint().getMethod();
        final String path = apiRequest.getApiEndpoint().getPath();

        final String body = apiRequest.getBody();

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




                try {
                    Log.d(LOG_TAG, path);
                    Log.d(LOG_TAG, "Invoking API w/ Request : " + request.getHttpMethod() + ":" + request.getPath());

                    long startTime = System.currentTimeMillis();

                    final ApiResponse response = client.execute(request);

                    final long latency = System.currentTimeMillis() - startTime;

                    final InputStream responseContentStream = response.getContent();

                    if (responseContentStream != null) {
                        final String responseData = IOUtils.toString(responseContentStream);

                        JSONObject responseJSON = new JSONObject(responseData);
                        baseDAO.set_DAO_BODY(responseJSON);
                        JSONObject json = baseDAO.get_DAO_BODY();
                        String status = "failure";

                        try {
                            status = json.getString("status");
                            Log.d(LOG_TAG, json.getString("status"));
                        } catch (JSONException e) {
                            Log.e(LOG_TAG, e.getMessage());
                        }

                        apiResponse = new APIResponse(status);
                        apiResponse.setResponseBody(baseDAO);
                        Log.d(LOG_TAG, "Response : " + baseDAO.get_DAO_BODY().toString());

                    } else {

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
            return apiResponse;
    }


    /**
     * run post-execution of the request
     * @param apiResponse
     */
    @Override
    public void onPostExecute(APIResponse apiResponse) {
        callBack.onResponseReceived(apiResponse);
    }

    public static interface ServiceCallBack {
        public void onResponseReceived(APIResponse<BaseDAO> apiResponse);
    }
}
