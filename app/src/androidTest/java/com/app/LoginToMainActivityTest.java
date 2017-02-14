package com.quebec.app;


import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.quebec.app.auth.SplashActivity;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.*;
import static android.support.test.espresso.assertion.ViewAssertions.*;
import static android.support.test.espresso.matcher.ViewMatchers.*;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class LoginToMainActivityTest {

    @Rule
    public ActivityTestRule<SplashActivity> mActivityTestRule = new ActivityTestRule<>(SplashActivity.class);

    @Test
    public void loginToMainActivityTest() {
        ViewInteraction editText = onView(
                allOf(withId(com.quebec.app.R.id.signIn_editText_email),
                        withParent(withId(com.quebec.app.R.id.linearLayout)),
                        isDisplayed()));
        editText.perform(click());

        ViewInteraction editText2 = onView(
                allOf(withId(com.quebec.app.R.id.signIn_editText_email),
                        withParent(withId(com.quebec.app.R.id.linearLayout)),
                        isDisplayed()));
        editText2.perform(replaceText("ad"), closeSoftKeyboard());

        ViewInteraction editText3 = onView(
                allOf(withId(com.quebec.app.R.id.signIn_editText_email), withText("ad"),
                        withParent(withId(com.quebec.app.R.id.linearLayout)),
                        isDisplayed()));
        editText3.perform(pressImeActionButton());

        ViewInteraction editText4 = onView(
                allOf(withId(com.quebec.app.R.id.signIn_editText_password),
                        withParent(withId(com.quebec.app.R.id.linearLayout)),
                        isDisplayed()));
        editText4.perform(replaceText("Hello@123"), closeSoftKeyboard());

        pressBack();

        ViewInteraction button = onView(
                allOf(withId(com.quebec.app.R.id.signIn_imageButton_login), withText("Login"),
                        withParent(withId(com.quebec.app.R.id.linearLayout)),
                        isDisplayed()));
        button.perform(click());

        ViewInteraction relativeLayout = onView(
                allOf(withId(com.quebec.app.R.id.activity_main_container),
                        childAtPosition(
                                allOf(withId(android.R.id.content),
                                        childAtPosition(
                                                withId(com.quebec.app.R.id.action_bar_root),
                                                0)),
                                0),
                        isDisplayed()));
        relativeLayout.check(matches(isDisplayed()));

    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
