package com.plim.kati_app;


import androidx.test.espresso.PerformException;
import androidx.test.espresso.contrib.PickerActions;
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
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class UserDataEditTest {

    public static final String TEST_EMAIL = "yunyen@mz.co.kr";
    public static final String TEST_PASSWORD = "123qwea";

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void init() throws InterruptedException {
        try {
            //로그인이 되어 있으면 바로 알러지 설정 프레그먼트까지 이동.
            onView(withId(R.id.action_mykati)).perform(click());
            Thread.sleep(1500);
            onView(withId(R.id.myKatiFragment_myInfoLayout)).perform(click());
        } catch (PerformException e) {
            //로그인이 되어 있지 않으면, 로그인 먼저 하기.
            onView(withId(R.id.action_mykati)).perform(click());
            onView(withId(R.id.myKatiFragment_loginButton)).perform(click());
            onView(withId(R.id.loginActivity_emaiEditText)).perform(typeText(TEST_EMAIL), closeSoftKeyboard());
            onView(withId(R.id.loginActivity_passwordEditText)).perform(typeText(TEST_PASSWORD), closeSoftKeyboard());
            onView(withId(R.id.loginActivity_loginButton)).perform(click());
            Thread.sleep(1500);
            onView(withId(R.id.myKatiFragment_myInfoLayout)).perform(click());
        }
    }

    @Test
    public void addressDataChangeTest() {
        String testName= "ggyeongi-do goyang-si";

        onView(withId(R.id.myInfoEditFragment_changeAddressButton)).perform(click());
        onView(withId(R.id.nameEditActivity_editText)).perform(typeText(testName), closeSoftKeyboard());
        onView(withId(R.id.nameEditActivity_submitButton)).perform(click());

        onView(withText(testName)).check(matches(isDisplayed()));
    }


    public void birthdayDataChangeTest() {
        int year=1997,month=11, date=11;
        onView(withId(R.id.myInfoEditFragment_changeBirthButton)).perform(click());
        onView(withId(R.id.nameEditActivity_editText)).perform(PickerActions.setDate(year,month,date));
        onView(withId(R.id.nameEditActivity_submitButton)).perform(click());

        onView(withText(year+"-"+month+"-"+date)).check(matches(isDisplayed()));
    }

    @Test
    public void nameDataChangeTest() {
        String testName= "TestNick";

        onView(withId(R.id.myInfoEditFragment_editNameButton)).perform(click());
        onView(withId(R.id.nameEditActivity_editText)).perform(typeText(testName), closeSoftKeyboard());
        onView(withId(R.id.nameEditActivity_submitButton)).perform(click());

        onView(withText(testName)).check(matches(isDisplayed()));
    }



}
