package com.plim.kati_app;


import androidx.test.espresso.PerformException;
import androidx.test.espresso.contrib.PickerActions;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;

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
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.plim.kati_app.testUtil.TestTool.USER_DATA_EDIT_TEST_ADDRESS;
import static com.plim.kati_app.testUtil.TestTool.USER_DATA_EDIT_TEST_NEW_NAME;
import static com.plim.kati_app.testUtil.TestTool.USER_EMAIL_YEEUN;
import static com.plim.kati_app.testUtil.TestTool.USER_PASSWORD_YEEUN;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class UserDataEditTest {

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void init() throws InterruptedException {
        try {
            //로그인이 되어 있으면 바로 알러지 설정 프레그먼트까지 이동.
            onView(withId(R.id.action_mykati)).perform(click());
            TestTool.sleep();
            onView(withId(R.id.myKatiFragment_myInfoLayout)).perform(click());
        } catch (PerformException e) {
            //로그인이 되어 있지 않으면, 로그인 먼저 하기.
            onView(withId(R.id.action_mykati)).perform(click());
            onView(withId(R.id.myKatiFragment_loginButton)).perform(click());
            onView(withId(R.id.loginActivity_emaiEditText)).perform(typeText(USER_EMAIL_YEEUN), closeSoftKeyboard());
            onView(withId(R.id.loginActivity_passwordEditText)).perform(typeText(USER_PASSWORD_YEEUN), closeSoftKeyboard());
            onView(withId(R.id.loginActivity_loginButton)).perform(click());
            TestTool.sleep();
            onView(withId(R.id.myKatiFragment_myInfoLayout)).perform(click());
        }
    }

    @Test
    public void addressDataChangeTest() {
        onView(withId(R.id.myInfoEditFragment_changeAddressButton)).perform(click());
        onView(withId(R.id.nameEditActivity_editText)).perform(typeText(USER_DATA_EDIT_TEST_ADDRESS), closeSoftKeyboard());
        onView(withId(R.id.nameEditActivity_submitButton)).perform(click());
        onView(withText(USER_DATA_EDIT_TEST_ADDRESS)).check(matches(isDisplayed()));
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
        onView(withId(R.id.myInfoEditFragment_editNameButton)).perform(click());
        onView(withId(R.id.nameEditActivity_editText)).perform(typeText(USER_DATA_EDIT_TEST_NEW_NAME), closeSoftKeyboard());
        onView(withId(R.id.nameEditActivity_submitButton)).perform(click());
        onView(withText(USER_DATA_EDIT_TEST_NEW_NAME)).check(matches(isDisplayed()));
    }



}
