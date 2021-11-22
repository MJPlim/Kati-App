package com.plim.kati_app;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.test.espresso.PerformException;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.uiautomator.By;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObject2;
import androidx.test.uiautomator.UiSelector;
import androidx.test.uiautomator.Until;

import com.plim.kati_app.kati.domain.main.MainActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class Kakao_LoginTest {
    private String email="plimtest@kakao.com";
    private String password="vmffla11!!";

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
    public void Kakao_LoginTest(){
        try {
            UiDevice uiDevice= UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
            onView(withId(R.id.loginActivity_kakoLoginButton)).perform(click());
            Thread.sleep(5000);
            //이미 가입되어있는 경우
            if(uiDevice.hasObject(By.text("동의하고 계속하기"))){
                new UiObject(new UiSelector().className( "android.widget.CheckBox" ).instance(0)).click();
                Thread.sleep(1000);
                new UiObject(new UiSelector().className( "android.widget.Button" ).instance(0)).click();
            }else{
                //처음 가입하는 경우
                new UiObject(new UiSelector().className( "android.widget.EditText" ).instance(0)).setText(email);
                new UiObject(new UiSelector().className( "android.widget.EditText" ).instance(1)).legacySetText(password);
                new UiObject(new UiSelector().className( "android.widget.Button" ).instance(0)).click();
                Thread.sleep(1000);
                new UiObject(new UiSelector().className( "android.widget.CheckBox" ).instance(0)).click();
                Thread.sleep(1000);
                new UiObject(new UiSelector().className( "android.widget.Button" ).instance(0)).click();
            }
            Thread.sleep(1000);
            onView(withId(R.id.fragment_mykati)).check(matches(isDisplayed()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
