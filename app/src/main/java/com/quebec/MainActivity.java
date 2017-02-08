//
// Copyright 2017 Amazon.com, Inc. or its affiliates (Amazon). All Rights Reserved.
//
// Code generated by AWS Mobile Hub. Amazon gives unlimited permission to 
// copy, distribute and modify it.
//
// Source code generated from template: aws-my-sample-app-android v0.14
//
package com.quebec;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.amazonaws.mobile.AWSMobileClient;
import com.amazonaws.mobile.user.IdentityManager;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class MainActivity extends AppCompatActivity implements View.OnClickListener,
                                                               EventsFeedFragment.EventsFeedInteractionListener,
                                                               EventDetailFragment.OnEventDetailInteractionListener,
                                                               ProfileFragment.ProfileInteractionListener,
                                                               FriendsListFragment.FriendsListInteractionHandler{

    /** Class name for log messages. */
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    /** The identity manager used to keep track of the current user account. */
    private IdentityManager identityManager;
    private Fragment mFragment;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Obtain a reference to the mobile client. It is created in the Application class,
        // but in case a custom Application class is not used, we initialize it here if necessary.
        AWSMobileClient.initializeMobileClientIfNecessary(this);

        // Obtain a reference to the mobile client. It is created in the Application class.
        final AWSMobileClient awsMobileClient = AWSMobileClient.defaultMobileClient();

        // Obtain a reference to the identity manager.
        identityManager = awsMobileClient.getIdentityManager();

        setContentView(R.layout.activity_main);

        if (findViewById(R.id.fragment_container) != null) {

            EventsFeedFragment eventsFeed = new EventsFeedFragment();
            eventsFeed.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, eventsFeed).commit();

            mFragment = eventsFeed;
        }

        BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {

            private Fragment frag;

            @Override
            public void onTabSelected(@IdRes int tabId) {

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

                switch (tabId) {
                    case R.id.menu_eventsfeed:
                        frag = new EventsFeedFragment();
                        transaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_left);
                        break;
                    case R.id.menu_uploadvideo:
                        showVideoUploadActivity();
                        break;
                    case R.id.menu_profile:
                        frag = new ProfileFragment();
                        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
                        break;
                }

                if (frag != null) {
                    Log.e("id", Integer.toString(frag.getId()));

                    transaction.remove(mFragment);
                    transaction.replace(R.id.fragment_container, frag);
                    transaction.commit();

                    mFragment = frag;
                }
            }
        });

    }

    public void showVideoUploadActivity() {
        Intent intent = new Intent(this, SignUpPhotoActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!AWSMobileClient.defaultMobileClient().getIdentityManager().isUserSignedIn()) {
            // In the case that the activity is restarted by the OS after the application
            // is killed we must redirect to the splash activity to handle the sign-in flow.
            Intent intent = new Intent(this, SplashActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            return;
        }

    }


    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        // Handle action bar item clicks here excluding the home button.

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(final Bundle bundle) {
        super.onSaveInstanceState(bundle);

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onClick(final View view) {

        // ... add any other button handling code here ...

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        }
        else {
            super.onBackPressed();
        }
    }


    @Override
    public void onBackToEvents() {

    }

    @Override
    public void onEventSelected(Event e) {
        EventDetailFragment eventDetail = EventDetailFragment.newInstance(e);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
        transaction.replace(R.id.fragment_container, eventDetail).addToBackStack("fragment");
        transaction.commit();
    }

    /* Profile page interactions. */

    @Override
    public void onFriendSelected(User u) {
        // TODO: Complete this method.
        Log.e("FRIND", "FRIEND");

    }

    @Override
    public void openFriendsList() {
        Log.e("HELLO", "friends list");
        FriendsListFragment friendsList = FriendsListFragment.newInstance();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
        transaction.replace(R.id.fragment_container, friendsList).addToBackStack("fragment");
        transaction.commit();
    }

    @Override
    public void openUpdateAccountDetails() {
        // TODO: Update this to be a seperate activity for updating account information.
        Intent intent = new Intent(this, SignUpPhotoActivity.class);
        startActivity(intent);
    }
}
