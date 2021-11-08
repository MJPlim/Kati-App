package com.plim.kati_app;

import android.content.ComponentName;
import android.content.Context;

import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.PerformException;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;

import com.plim.kati_app.kati.domain.login.LoginActivity;
import com.plim.kati_app.kati.domain.main.MainActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.*;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class LoginTest {

    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void init(){
        onView(withId(R.id.action_mykati)).perform(click());
        try {
            onView(withId(R.id.myKatiFragment_loginButton)).perform(click());
        } catch (PerformException e) {
            onView(withId(R.id.myKatiFragment_myInfoLayout)).perform(click());
            onView(withId(R.id.myInfoEditFragment_logOutTextView)).perform(click());
            onView(withId(android.R.id.button1)).perform(click());
            onView(withId(R.id.action_mykati)).perform(click());
            onView(withId(R.id.myKatiFragment_loginButton)).perform(click());
        }
    }

    @Test
    public void logInTest(){
        onView(withId(R.id.loginActivity_emaiEditText)).perform(typeText("sh199919@gmail.com"), closeSoftKeyboard());
        onView(withId(R.id.loginActivity_passwordEditText)).perform(typeText("1234567"), closeSoftKeyboard());
        onView(withId(R.id.loginActivity_loginButton)).perform(click());

        onView(withId(R.id.activity_main)).check(matches(isDisplayed()));
    }

    @Test
    public void logInFailTest(){
        onView(withId(R.id.loginActivity_emaiEditText)).perform(typeText("hello"), closeSoftKeyboard());
        onView(withId(R.id.loginActivity_passwordEditText)).perform(typeText("1234"), closeSoftKeyboard());
        onView(withId(R.id.loginActivity_loginButton)).perform(click());

        onView(withText("해당하는 유저가 없습니다.")).check(matches(isDisplayed()));
    }
}
