package com.quebec.services;


/**
 * Created by Andy on 14/02/2017.
 */

public interface APICallback<T> {

    public void onSuccess(T responseBody);

    public void onFailure(String message);


}
