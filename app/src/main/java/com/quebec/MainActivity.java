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
import com.roughike.bottombar.OnTabReselectListener;
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
    private String currentFragmentName;

    private void setFragment(Fragment frag, int transition) {

        if (!frag.getClass().getName().equals(currentFragmentName)) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            if (transition == 0) {
                transaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_left);
            }
            else {
                transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
            }
            transaction.remove(mFragment);
            transaction.replace(R.id.fragment_container, frag);
            transaction.addToBackStack(frag.getClass().getName());
            transaction.commit();
            currentFragmentName = frag.getClass().getName();
        }


    }

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
                        setFragment(new EventsFeedFragment(), 0);
                        break;
                    case R.id.menu_uploadvideo:
                        showVideoUploadActivity();
                        break;
                    case R.id.menu_profile:
                        setFragment(new ProfileFragment(), 1);
                        break;
                }
            }

        });

        bottomBar.setOnTabReselectListener(new OnTabReselectListener() {

            private Fragment frag;

            @Override
            public void onTabReSelected(@IdRes int tabId) {

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();


                switch (tabId) {
                    case R.id.menu_eventsfeed:
                        setFragment(new EventsFeedFragment(), 0);
                        break;
                    case R.id.menu_uploadvideo:
                        showVideoUploadActivity();
                        break;
                    case R.id.menu_profile:
                        setFragment(new ProfileFragment(), 0);
                        break;
                }


            }
        });

    }

    public void showVideoUploadActivity() {
        Intent intent = new Intent(this, VideoUploadActivity.class);
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
        setFragment(EventDetailFragment.newInstance(e), 1);
    }

    /* Profile page interactions. */

    @Override
    public void onFriendSelected(User u) {
        // TODO: Complete this method.
    }

    @Override
    public void openFriendsList() {
        setFragment(FriendsListFragment.newInstance(), 1);
    }

    @Override
    public void updateProfilePictureActivity() {
        Intent intent = new Intent(this, ProfilePictureSignUpActivity.class);
        startActivity(intent);
    }
}
