package com.plim.kati_app;

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
import static androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.AllOf.allOf;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class UserEditTest {

    private static final String TEST_NAME = "JSH";

    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void init(){
        onView(withId(R.id.action_mykati)).perform(click());
        try {
            onView(withId(R.id.myKatiFragment_myInfoLayout)).perform(click());
        } catch (PerformException e) {
            onView(withId(R.id.myKatiFragment_loginButton)).perform(click());
            onView(withId(R.id.loginActivity_emaiEditText)).perform(typeText("sh199919@gmail.com"), closeSoftKeyboard());
            onView(withId(R.id.loginActivity_passwordEditText)).perform(typeText("1234567"), closeSoftKeyboard());
            onView(withId(R.id.loginActivity_loginButton)).perform(click());
            onView(withId(R.id.action_mykati)).perform(click());
            onView(withId(R.id.myKatiFragment_myInfoLayout)).perform(click());
        }
    }

    @Test
    public void editNameTest(){
        onView(withId(R.id.myInfoEditFragment_editNameButton)).perform(click());
        onView(withId(R.id.nameEditActivity_editText)).perform(typeText(TEST_NAME), closeSoftKeyboard());
        onView(withId(R.id.nameEditActivity_submitButton)).perform(click());
        onView(allOf(withId(R.id.content), isDescendantOfA(withId(R.id.myInfoEditFragment_changeNameItem))))
                .check(matches(withText(TEST_NAME)));
    }
}
