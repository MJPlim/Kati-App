package com.plim.kati_app;

import static android.view.KeyEvent.KEYCODE_PERIOD;
import static android.view.KeyEvent.KEYCODE_POUND;
import static android.view.KeyEvent.KEYCODE_SEMICOLON;
import static android.view.KeyEvent.KEYCODE_SLASH;
import static android.view.KeyEvent.KEYCODE_STAR;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.espresso.PerformException;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.uiautomator.By;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiSelector;

import com.plim.kati_app.kati.domain.main.MainActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class Naver_LoginTest {
    private UiDevice uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
    private String email = "plimtest@naver.com";
    private String password = "vmffla11!!";

    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class);


    @Before
    public void init() {
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
    public void Naver_LoginTest() {
        try {
            onView(withId(R.id.loginActivity_naverLoginButton)).perform(click());
            Thread.sleep(1000);
            //이미 가입되어있는 경우
            if (uiDevice.hasObject(By.text("동의하기"))) {
                new UiObject(new UiSelector().className( "android.widget.Button" ).instance(1)).click();
            } else {
                //처음 가입하는 경우
                new UiObject(new UiSelector().className( "android.widget.EditText" ).instance(0)).setText(email);
                Thread.sleep(1000);
                new UiObject(new UiSelector().className( "android.widget.EditText" ).instance(1)).setText(password);
                Thread.sleep(1000);
                new UiObject(new UiSelector().className( "android.widget.Button" ).instance(3)).click();
                Thread.sleep(1000);
                new UiObject(new UiSelector().className( "android.widget.Button" ).instance(1)).click();
            }
            Thread.sleep(5000);
            onView(withId(R.id.fragment_mykati)).check(matches(isDisplayed()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}