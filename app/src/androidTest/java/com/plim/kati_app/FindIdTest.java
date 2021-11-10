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
public class FindIdTest {

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
    public void findId(){
        onView(withId(R.id.loginActivity_findIdTextView)).perform(click());
        onView(withId(R.id.idFindActivity_emailEditText)).perform(typeText("sh199919@naver.com"), closeSoftKeyboard());
        onView(withId(R.id.idFindActivity_sumbitButton)).perform(click());
        for(int i=0; i<5; i++){
            try{
                onView(withText("복구 이메일으로 기존 아이디(이메일)가 발송되었습니다.")).check(matches(isDisplayed()));
                break;
            }catch (NoMatchingViewException e){
                try { Thread.sleep(1000); } catch (InterruptedException ignored) { }
            }
        }
        onView(withId(android.R.id.button1)).perform(click());
        onView(withId(R.id.activity_main)).check(matches(isDisplayed()));
    }
}
