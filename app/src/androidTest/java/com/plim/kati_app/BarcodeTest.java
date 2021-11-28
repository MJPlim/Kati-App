package com.plim.kati_app;

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
import static androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.AllOf.allOf;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class BarcodeTest {

    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void test(){
        onView(withId(R.id.action_search)).perform(click());
        onView(withId(R.id.searchFragment_barcodeSearchIcon)).perform(click());
        sleep(5000); // 5초 안에 찍어!
        onView(withId(R.id.foodItemFragment_foodNameTextView)).check(matches(withText("3분쇠고기카레")));
    }

    private void sleep(int time){
        try { Thread.sleep(time); } catch (InterruptedException ignored) { }
    }
}
