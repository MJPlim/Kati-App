package com.plim.kati_app;

import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.PerformException;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;

import com.plim.kati_app.kati.domain.main.MainActivity;

import junit.framework.AssertionFailedError;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class LogoutTest {

    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void init(){
        onView(withId(R.id.action_mykati)).perform(click());
        try {
            onView(withId(R.id.myKatiFragment_myInfoLayout)).check(matches(isDisplayed()));
        } catch (AssertionFailedError e) {
            onView(withId(R.id.myKatiFragment_loginButton)).perform(click());
            onView(withId(R.id.loginActivity_emaiEditText)).perform(typeText("sh199919@gmail.com"), closeSoftKeyboard());
            onView(withId(R.id.loginActivity_passwordEditText)).perform(typeText("1234567"), closeSoftKeyboard());
            onView(withId(R.id.loginActivity_loginButton)).perform(click());
        }
        onView(withId(R.id.myKatiFragment_myInfoLayout)).perform(click());
    }

    @Test
    public void logOutTest(){
        onView(withId(R.id.myInfoEditFragment_logOutTextView)).perform(click());
        onView(withId(android.R.id.button1)).perform(click());
        onView(withId(R.id.action_mykati)).check(matches(isDisplayed()));
    }
}
