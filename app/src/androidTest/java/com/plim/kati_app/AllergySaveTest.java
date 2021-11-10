package com.plim.kati_app;


import androidx.test.espresso.Espresso;
import androidx.test.espresso.PerformException;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;

import com.plim.kati_app.kati.domain.main.MainActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isChecked;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class AllergySaveTest {

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void init() {
        try {
            //로그인이 되어 있으면 바로 알러지 설정 프레그먼트까지 이동.
            onView(withId(R.id.action_mykati)).perform(click());
            onView(withId(R.id.myKatiFragment_allergyItem)).perform(click());
        } catch (PerformException e) {
            //로그인이 되어 있지 않으면, 로그인 먼저 하기.
            onView(withId(R.id.action_mykati)).perform(click());
            onView(withId(R.id.myKatiFragment_loginButton)).perform(click());
            onView(withId(R.id.loginActivity_emaiEditText)).perform(typeText("sh199919@gmail.com"), closeSoftKeyboard());
            onView(withId(R.id.loginActivity_passwordEditText)).perform(typeText("1234567"), closeSoftKeyboard());
            onView(withId(R.id.loginActivity_loginButton)).perform(click());
            onView(withId(R.id.myKatiFragment_allergyItem)).perform(click());
        }
    }

    @Test
    public void allergySaveTest() {
        //두 가지 체크하기, 우유와 아몬드.
        onView(withId(100000)).perform(click());
        Espresso.pressBack();

        //다시 들어와서 우유와 아몬드 체크 확인하기.
        onView(withId(R.id.myKatiFragment_allergyItem)).perform(click());
        onView(withId(100000)).check(matches(isChecked()));

        //우유와 아몬드 체크 지우기.
        onView(withId(100000)).perform(click());
    }


}
