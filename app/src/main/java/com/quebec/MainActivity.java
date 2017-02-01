//
// Copyright 2017 Amazon.com, Inc. or its affiliates (Amazon). All Rights Reserved.
//
// Code generated by AWS Mobile Hub. Amazon gives unlimited permission to 
// copy, distribute and modify it.
//
// Source code generated from template: aws-my-sample-app-android v0.14
//
package com.quebec;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import com.amazonaws.mobile.AWSMobileClient;
import com.amazonaws.mobile.user.IdentityManager;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;


public class MainActivity extends AuthActivity implements View.OnClickListener,
                                                               EventsFeedFragment.EventsFeedInteractionListener,
                                                               EventDetailFragment.OnEventDetailInteractionListener,
                                                               ProfileFragment.ProfileInteractionListener {

    /** Class name for log messages. */
    private static final String LOG_TAG = MainActivity.class.getSimpleName();


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        if (findViewById(R.id.fragment_container) != null) {

            EventsFeedFragment eventsFeed = new EventsFeedFragment();
            eventsFeed.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, eventsFeed).commit();

        }

        setupBottomBar();



    }

    /**
     * Setups the bottom navigation with appropriate event handling for each tab icon.
     */
    protected void setupBottomBar() {
        BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {

            private Fragment frag;

            @Override
            public void onTabSelected(@IdRes int tabId) {

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);

                switch (tabId) {
                    case R.id.menu_eventsfeed:
                        frag = new EventsFeedFragment();
                        break;
                    case R.id.menu_uploadvideo:
                        showVideoUploadActivity();
                        break;
                    case R.id.menu_profile:
                        frag = new ProfileFragment();
                        break;
                }

                if (frag != null) {
                    transaction.replace(R.id.fragment_container, frag);
                    transaction.commit();
                }
            }
        });
    }

    protected void showVideoUploadActivity() {
        Intent intent = new Intent(this, VideoUploadActivity.class);
        startActivity(intent);
    }

    /**
     * Handles the resuming of the application from a paused state. Checks
     */
    @Override
    protected void onResume() {
        super.onResume();
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
    public void onClick(final View view) {

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    /*

    Fragment implementations
    ----
     */


    /**
     * From the EventsFeedFragment, an event has been selected and we should now
     * transition to the EventsDetailFragment.
     */
    @Override
    public void onEventSelected() {

        EventDetailFragment eventDetail = new EventDetailFragment();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
        transaction.replace(R.id.fragment_container, eventDetail);
        transaction.commit();

    }

    /**
     * From the EventsDetailFragment, the back button has been selected and we should now
     * transition back to the EventsFeedFragment.
     */
    @Override
    public void onBackToEvents() {

    }
}
