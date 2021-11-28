package com.plim.kati_app;


import androidx.test.espresso.Espresso;
import androidx.test.espresso.PerformException;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;

import com.plim.kati_app.kati.domain.main.MainActivity;
import com.plim.kati_app.testUtil.TestTool;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.plim.kati_app.testUtil.TestTool.FAV_TEST_FOOD_NAME;
import static com.plim.kati_app.testUtil.TestTool.USER_EMAIL_YEEUN;
import static com.plim.kati_app.testUtil.TestTool.USER_PASSWORD_YEEUN;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class favTest {


    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void init() throws InterruptedException {
        //로그인이 된 채로, 검색창까지 이동.
        try {
            onView(withId(R.id.action_favorite)).perform(click());
            onView(withId(R.id.favoriteFragment_loginButton)).perform(click());
            onView(withId(R.id.loginActivity_emaiEditText)).perform(typeText(USER_EMAIL_YEEUN), closeSoftKeyboard());
            onView(withId(R.id.loginActivity_passwordEditText)).perform(typeText(USER_PASSWORD_YEEUN), closeSoftKeyboard());
            onView(withId(R.id.loginActivity_loginButton)).perform(click());
        } catch (PerformException e) { }
        
        TestTool.sleep();
        onView(withId(R.id.action_search)).perform(click());
        TestTool.sleep();
        onView(withId(20024)).perform(click());
        TestTool.sleep();
        onView(withId(R.id.newFoodItem_productNameTextView)).perform(click());
    }

    @Test
    public void addFav() throws InterruptedException, UiObjectNotFoundException {
        onView(withId(R.id.foodItemFragment_heartButton)).perform(click());
        UiDevice uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        UiObject button = uiDevice.findObject(new UiSelector().text("확인"));
        if (button.exists() && button.isEnabled()) {
            button.click();
        }
        Espresso.pressBack();
        onView(withId(R.id.action_favorite)).perform(click());
        TestTool.sleep();
        onView(withText(FAV_TEST_FOOD_NAME)).check(matches(isDisplayed()));


        onView(withText(FAV_TEST_FOOD_NAME)).perform(click());
        TestTool.sleep();
        onView(withId(R.id.foodItemFragment_heartButton)).perform(click());

        Espresso.pressBack();
        onView(withId(R.id.action_favorite)).perform(click());
        TestTool.sleep();
        onView(withText(FAV_TEST_FOOD_NAME)).check(doesNotExist());
    }

}
