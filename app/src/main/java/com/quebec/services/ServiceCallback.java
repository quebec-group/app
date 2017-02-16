package com.quebec.services;

/**
 * Created by Andy on 16/02/2017.
 */

public interface ServiceCallback<T> {
    public void onSuccess(T responseBody);

    public void onFailure(String message);
}
