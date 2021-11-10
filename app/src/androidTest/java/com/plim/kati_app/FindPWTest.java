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
public class FindPWTest {

    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class);

    static final String NEW_PASSWORD = "YKM9S7HL";

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
    public void getNewPassword(){
        onView(withId(R.id.loginActivity_findPWTextView)).perform(click());
        onView(withId(R.id.passwordFindActivity_restoreEmailEditText)).perform(typeText("sh199919@gmail.com"), closeSoftKeyboard());
        onView(withId(R.id.passwordFindActivity_submitButton)).perform(click());
        for(int i=0; i<5; i++){
            try{
                onView(withText("임시 비밀번호를 발급하였습니다.")).check(matches(isDisplayed()));
                break;
            }catch (NoMatchingViewException e){
                try { Thread.sleep(1000); } catch (InterruptedException ignored) { }
            }
        }
        onView(withId(android.R.id.button1)).perform(click());
        onView(withId(R.id.activity_main)).check(matches(isDisplayed()));
    }

    @Test
    public void loginWithNewPassword(){
        onView(withId(R.id.loginActivity_emaiEditText)).perform(typeText("sh199919@gmail.com"), closeSoftKeyboard());
        onView(withId(R.id.loginActivity_passwordEditText)).perform(typeText(NEW_PASSWORD), closeSoftKeyboard());
        onView(withId(R.id.loginActivity_loginButton)).perform(click());

        onView(withId(R.id.activity_main)).check(matches(isDisplayed()));
    }
}
