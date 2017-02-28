package com.app;

import android.util.Log;

import com.quebec.app.User;
import com.quebec.services.APICallback;
import com.quebec.services.APIManager;

import org.junit.Test;

import java.util.List;

import static junit.framework.Assert.assertEquals;

/**
 * Created by Andy on 23/02/2017.
 *
 * Unit testing for Services layer
 */

public class ServicesTester {

    private final String _TEST_USERNAME = "TestUser";
    private final String _TEST_EMAIL = "test@email.com";

//    final AmazonS3Client mockedClient = Mockito.mock(AmazonS3Client.class);
//    final S3Object mockedS3Obj = Mockito.mock(S3Object.class);

    @Test
    public void createUserTest() {
        APICallback<String> userAPICallback = new APICallback<String>() {
                            @Override
                            public void onSuccess(String responseBody) {
                                Log.d("createUserTest", "success");
                            }

                            @Override
                            public void onFailure(String message) {

                            }
                        };

        APIManager.getInstance().createUser("John Lennon", "hello@gmail.com", userAPICallback);
}

    @Test
    public void getFriendsTest() {
        APICallback<List<User>> test = new APICallback<List<User>>() {
            @Override
            public void onSuccess(List<User> responseBody) {
                System.out.println(responseBody.get(0));
                assertEquals(responseBody, "s");
            }

            @Override
            public void onFailure(String message) {

            }
        };
        APIManager.getInstance().followers("1", test);
    }
}
