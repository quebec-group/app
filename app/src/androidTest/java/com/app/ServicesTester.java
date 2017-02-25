package com.app;

import android.util.Log;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.S3Object;
import com.quebec.app.User;
import com.quebec.services.APICallback;
import com.quebec.services.APIManager;


import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;

/**
 * Created by Andy on 23/02/2017.
 *
 * Unit testing for Services layer
 */

public class ServicesTester {

    private final String _TEST_USERNAME = "TestUser";
    private final String _TEST_EMAIL = "test@email.com";

    final AmazonS3Client mockedClient = Mockito.mock(AmazonS3Client.class);
    final S3Object mockedS3Obj = Mockito.mock(S3Object.class);



    @Test
    public void createUserTest() {
        APICallback<User> userAPICallback = new APICallback<User>() {
                            @Override
                            public void onSuccess(User responseBody) {
                                Log.d("createUserTest", "success");
                            }

                            @Override
                            public void onFailure(String message) {

                            }
                        };

                        APIManager apiManager = new APIManager();
                        apiManager.createUser("John Lennon", "hello@gmail.com", userAPICallback);
}

    @Test
    public void getFriendsTest() {
        APICallback<ArrayList<User>> test = new APICallback<ArrayList<User>>() {
            @Override
            public void onSuccess(ArrayList<User> responseBody) {
                System.out.println(responseBody.get(0));
                assertEquals(responseBody, "s");
            }

            @Override
            public void onFailure(String message) {

            }
        };
        APIManager apiManager = new APIManager();
        apiManager.getFollowers(test);
    }
}
