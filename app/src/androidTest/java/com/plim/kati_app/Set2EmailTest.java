package com.plim.kati_app;

import android.os.IBinder;
import android.view.WindowManager;

import androidx.test.espresso.Root;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;

import com.plim.kati_app.kati.domain.main.MainActivity;

import junit.framework.AssertionFailedError;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.app.PendingIntent.getActivity;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.AllOf.allOf;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class Set2EmailTest {

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
        sleep();
        onView(withId(R.id.myKatiFragment_myInfoLayout)).perform(click());
    }

    @Test
    public void set2EmailTest(){
        set2Email("start@naver.com");
        sleep();
        set2Email("sh199919@naver.com");
        sleep();
        onView(allOf(withId(R.id.content), isDescendantOfA(withId(R.id.myInfoEditFragment_restoreEmailItem))))
                .check(matches(withText("sh199919@naver.com")));
    }
    @Test
    public void set2EmailFailTest(){
        set2Email("start@naver.com");
        sleep();
        set2Email("sh199919@naver.com");
        sleep();
        set2Email("sh199919@naver.com");
        sleep();
        onView(withId(R.id.activity_restore_email)).check(matches(isDisplayed()));
    }

    private void set2Email(String s) {
        onView(withId(R.id.myInfoEditFragment_restoreEmailButton)).perform(click());
        onView(withId(R.id.restoreEmailActivity_restoreEmailEditText)).perform(typeText(s), closeSoftKeyboard());
        onView(withId(R.id.restoreEmailActivity_submitButton)).perform(click());
    }

    private void sleep(){
        try { Thread.sleep(1000); } catch (InterruptedException ignored) { }
    }
}
