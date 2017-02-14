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
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.pressImeActionButton;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class LoginFailedActivityTest {

    @Rule
    public ActivityTestRule<SplashActivity> mActivityTestRule = new ActivityTestRule<>(SplashActivity.class);

    @Test
    public void loginFailedActivityTest() {
        ViewInteraction editText = onView(
                allOf(withId(R.id.signIn_editText_email),
                        withParent(withId(R.id.linearLayout)),
                        isDisplayed()));
        editText.perform(click());

        ViewInteraction editText2 = onView(
                allOf(withId(R.id.signIn_editText_email),
                        withParent(withId(R.id.linearLayout)),
                        isDisplayed()));
        editText2.perform(click());

        ViewInteraction editText3 = onView(
                allOf(withId(R.id.signIn_editText_email),
                        withParent(withId(R.id.linearLayout)),
                        isDisplayed()));
        editText3.perform(replaceText("ad"), closeSoftKeyboard());

        ViewInteraction editText4 = onView(
                allOf(withId(R.id.signIn_editText_email), withText("ad"),
                        withParent(withId(R.id.linearLayout)),
                        isDisplayed()));
        editText4.perform(pressImeActionButton());

        ViewInteraction editText5 = onView(
                allOf(withId(R.id.signIn_editText_password),
                        withParent(withId(R.id.linearLayout)),
                        isDisplayed()));
        editText5.perform(replaceText("fail"), closeSoftKeyboard());

        ViewInteraction editText6 = onView(
                allOf(withId(R.id.signIn_editText_password), withText("fail"),
                        withParent(withId(R.id.linearLayout)),
                        isDisplayed()));
        editText6.perform(pressImeActionButton());

        ViewInteraction button = onView(
                allOf(withId(android.R.id.button3), withText("OK"),
                        withParent(allOf(withClassName(is("com.android.internal.widget.ButtonBarLayout")),
                                withParent(withClassName(is("android.widget.LinearLayout"))))),
                        isDisplayed()));
        button.perform(click());

        pressBack();

        ViewInteraction textView = onView(
                allOf(withId(R.id.signin_error), withText("Sign-in failed. Please try again."),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                3),
                        isDisplayed()));
        textView.check(matches(withText("Sign-in failed. Please try again.")));

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
