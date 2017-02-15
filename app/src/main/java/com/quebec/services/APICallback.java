package com.quebec.services;

import com.amazonaws.mobileconnectors.apigateway.ApiResponse;

/**
 * Created by Andy on 14/02/2017.
 */

public interface APICallback<T> {

    public abstract void onSuccess(T responseBody);

    public abstract void onFailure(String message);


}
