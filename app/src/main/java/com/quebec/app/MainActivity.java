//
// Copyright 2017 Amazon.com, Inc. or its affiliates (Amazon). All Rights Reserved.
//
// Code generated by AWS Mobile Hub. Amazon gives unlimited permission to
// copy, distribute and modify it.
//
// Source code generated from template: aws-my-sample-app-android v0.14
//
package com.quebec.app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.amazonaws.mobile.AWSMobileClient;
import com.amazonaws.mobile.user.IdentityManager;
import com.quebec.app.auth.SplashActivity;
import com.quebec.services.APICallback;
import com.quebec.services.APIManager;
import com.quebec.services.AWSWrapper;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.quebec.app.EventVideoUploadSelect.EVENT_VIDEO_MODE;


public class MainActivity extends AppCompatActivity implements View.OnClickListener,
        EventsFeedFragment.EventsFeedInteractionListener,
        EventDetailFragment.OnEventDetailInteractionListener,
        ProfileFragment.ProfileInteractionListener,
        FriendsListFragment.FriendsListInteractionHandler,
        ProfileFriendFragment.OnFragmentInteractionListener {

    /** The identity manager used to keep track of the current user account. */
    private IdentityManager identityManager;
    private Fragment mFragment;
    private String currentFragmentTab;
    private static String LOG_TAG = MainActivity.class.getSimpleName();
    private SharedPreferences sharedPreferences;
    private User current_user;

    private BottomBar mBottomBar;
    public static String EVENT_ITEM_KEY;

    /**
     * Change the current fragment in the main activity view.
     * @param frag
     * @param transition
     */
    private void setFragment(Fragment frag, FragmentTransition transition, boolean addToBackStack) {
        currentFragmentTab = frag.getClass().getName();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.setCustomAnimations(transition.enter, transition.exit, transition.popEnter, transition.popExit);

        transaction.replace(R.id.fragment_container, frag);

        transaction.addToBackStack(frag.getClass().getName());

        transaction.commitAllowingStateLoss();
        currentBottomBarItem = -1;

        mFragment = frag;
    }

    private int previousBottomBarItemIndex = 0;
    private int currentBottomBarItem;

    private void setBottomBarFragment(int tabId) {

        if (currentBottomBarItem == tabId) { return; }

        switch (tabId) {
            case R.id.menu_eventsfeed:
                setFragment(new EventsFeedFragment(), new FragmentTransitionFromLeft(), false);
                break;
            case R.id.menu_uploadvideo:

                /* A fix for the video upload tab. Requires a timer as a workaround. */
                Handler handlerTimer = new Handler();
                handlerTimer.postDelayed(new Runnable(){
                    public void run() {
                        mBottomBar.selectTabAtPosition(previousBottomBarItemIndex);
                    }}, 500);

                showVideoUploadActivity();
                break;
            case R.id.menu_profile:
                setFragment(new ProfileFragment(), new FragmentTransitionFromRight(), false);
                break;
        }


        previousBottomBarItemIndex = mBottomBar.getCurrentTabPosition();
        currentBottomBarItem = tabId;
    }

    private void checkUserFaceTrained() {
        APIManager.getInstance().hasCompletedSignUp(new APICallback<Boolean>() {
            @Override
            public void onSuccess(Boolean responseBody) {
                if (!responseBody) {
                    // User has not completed the sign and hence must go to the signup.
                    showFaceTrainingActivity();
                }
            }

            @Override
            public void onFailure(String message) {

            }
        });
    }

    /**
     * Open the face training video page.
     */
    private void showFaceTrainingActivity() {
        Intent intent = new Intent(this, SignUpVideoActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Obtain a reference to the mobile client. It is created in the Application class,
        // but in case a custom Application class is not used, we initialize it here if necessary.
        AWSMobileClient.initializeMobileClientIfNecessary(this);

        // Obtain a reference to the mobile client. It is created in the Application class.
        final AWSMobileClient awsMobileClient = AWSMobileClient.defaultMobileClient();

        // Obtain a reference to the identity manager.
        identityManager = awsMobileClient.getIdentityManager();

        sharedPreferences = getSharedPreferences("SignUp", Context.MODE_PRIVATE);

        APIManager.getInstance().createUser(
                sharedPreferences.getString("givenName", ""),
                sharedPreferences.getString("email", ""),
                new APICallback<String>() {
                    @Override
                    public void onSuccess(String responseBody) {
                        Log.e(LOG_TAG, "User successfully added");
                    }

                    @Override
                    public void onFailure(String message) {
                        Log.e(LOG_TAG, "Couldn't add user");
                    }
                });

        if (findViewById(R.id.fragment_container) != null) {

            EventsFeedFragment eventsFeed = new EventsFeedFragment();
            eventsFeed.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, eventsFeed).commit();

            mFragment = eventsFeed;
        }

        ProfilePictureHandler profilePictureHandler = new ProfilePictureHandler();
        if (!profilePictureHandler.profilePictureExists()) {
            Intent intent = new Intent(this, ProfilePictureUpdateActivity.class);
            startActivity(intent);
        }

        mBottomBar = (BottomBar) findViewById(R.id.bottomBar);

        /* Handle the reselection of a bottom bar tab. */
        mBottomBar.setOnTabReselectListener(new OnTabReselectListener() {
            @Override
            public void onTabReSelected(@IdRes int tabId) {
                setBottomBarFragment(tabId);
            }
        });

        /* Handle the single selection of a bottom bar tab. */
        mBottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes final int tabId) {
                setBottomBarFragment(tabId);
            }

        });

    }


    /**
     * onStart for the video. Checks for whether the user has uploaded a video
     * for facial training.
     */
    @Override
    public void onStart() {
        super.onStart();
        // checkUserFaceTrained();
    }

    /**
     * Handles opening the video upload panel.
     */
    public void showVideoUploadActivity() {
        Intent intent = new Intent(this, EventVideoUploadSelect.class);
        intent.putExtra(EVENT_VIDEO_MODE, 0);
        startActivity(intent);
    }

    @Override
    protected void onResume() {

        /* Refresh the fragment view. */
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.detach(mFragment).attach(mFragment).commit();

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
    public void onEventSelected(Event e) {
        setFragment(EventDetailFragment.newInstance(e), new FragmentTransitionFromRight(), true);
    }

    /* Profile page interactions. */

    @Override
    public void onFriendSelected(User u) {
        // TODO: Complete this method.
        setFragment(ProfileFriendFragment.newInstance(u), new FragmentTransitionFromRight(), true);
    }

    @Override
    public void openFollowingList() {
        RelatedUsersListFragment.UsersRelation usersRelation = RelatedUsersListFragment.UsersRelation.FOLLOWING;
        setFragment(RelatedUsersListFragment.newInstance(usersRelation, this), new FragmentTransitionFromRight(), true);
    }

    @Override
    public void openFollowersList() {
        RelatedUsersListFragment.UsersRelation usersRelation = RelatedUsersListFragment.UsersRelation.FOLLOWERS;
        setFragment(RelatedUsersListFragment.newInstance(usersRelation, this), new FragmentTransitionFromRight(), true);
    }

    @Override
    public void openAboutPage() {
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
    }

    @Override
    public void updateProfilePictureActivity() {
        Intent intent = new Intent(this, ProfilePictureUpdateActivity.class);
        startActivity(intent);
    }

    @Override
    public void onProfileEventSelected(Event e) {
        setFragment(EventDetailFragment.newInstance(e), new FragmentTransitionFromRight(), true);
    }

    /** Events for the event detail page. **/
    @Override
    public void onEventUserSelected(User u) {
        onUserSelected(u);
    }

    @Override
    public void openEventDetailLocation(Event e) {
        Intent intent = new Intent(this, EventDetailMapActivity.class);
        intent.putExtra(EVENT_ITEM_KEY, e);
        startActivity(intent);
    }



    @Override
    public void openUserSelector(Event event) {
        setFragment(AddUserListFragment.newInstance(event, this), new FragmentTransitionFromRight(), true);
    }

    @Override
    public void onProfileFriendEventSelected(Event event) {
        setFragment(EventDetailFragment.newInstance(event), new FragmentTransitionFromRight(), true);
    }


    public void onUserSelected(User user) {
        String us = sharedPreferences.getString("userID","");

        Log.d(LOG_TAG, "u:" + user.getUserID() + "curr" +  AWSWrapper.getCognitoID());

        if(user.getUserID().equals(AWSWrapper.getCognitoID())) {
            setFragment(ProfileFragment.newInstance(), new FragmentTransitionFromRight(), true);
            currentBottomBarItem  = R.id.menu_profile;
        } else {
            setFragment(ProfileFriendFragment.newInstance(user), new FragmentTransitionFromRight(), true);
        }
    }
}