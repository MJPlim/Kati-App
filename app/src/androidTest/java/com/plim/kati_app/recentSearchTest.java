package com.plim.kati_app;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;

import com.plim.kati_app.kati.domain.main.MainActivity;
import com.plim.kati_app.testUtil.ClickCloseIconAction;


import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.pressImeActionButton;
import static androidx.test.espresso.action.ViewActions.pressKey;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.plim.kati_app.testUtil.TestTool.RECENT_SEARCH_TEST_SEARCH_WORD_1;
import static com.plim.kati_app.testUtil.TestTool.RECENT_SEARCH_TEST_SEARCH_WORD_2;
import static com.plim.kati_app.testUtil.TestTool.RECENT_SEARCH_TEST_SEARCH_WORD_3;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class recentSearchTest {

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void init() {
        //최근검색어 프래그먼트까지 이동.
        onView(withId(R.id.action_search)).perform(click());
    }

    @Test
    public void doClickAndCheckSearchedAndAdded() throws InterruptedException {
        onView(withId(R.id.searchFragment_searchFieldEditText)).perform(typeText(RECENT_SEARCH_TEST_SEARCH_WORD_1),pressImeActionButton());
        onView(withId(R.id.action_search)).perform(click());
        onView(withText(RECENT_SEARCH_TEST_SEARCH_WORD_1)).check(matches(isDisplayed()));

        onView(withId(100001)).check(matches(isDisplayed()));
    }

    @Test
    public void deleteOneAndCheckDeleted() throws UiObjectNotFoundException {
        onView(withId(R.id.searchFragment_searchFieldEditText)).perform(typeText(RECENT_SEARCH_TEST_SEARCH_WORD_1),pressImeActionButton());
        onView(withId(R.id.action_search)).perform(click());

        onView(withId(R.id.searchFragment_searchFieldEditText)).perform(typeText(RECENT_SEARCH_TEST_SEARCH_WORD_2),pressImeActionButton());
        onView(withId(R.id.action_search)).perform(click());

        onView(withId(R.id.searchFragment_searchFieldEditText)).perform(typeText(RECENT_SEARCH_TEST_SEARCH_WORD_3),pressImeActionButton());
        onView(withId(R.id.action_search)).perform(click());

        onView(withId(100003)).perform(new ClickCloseIconAction());
        UiDevice uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        UiObject button = uiDevice.findObject(new UiSelector().text("확인"));
        if (button.exists() && button.isEnabled()) {
            button.click();
        }

        onView(withText(RECENT_SEARCH_TEST_SEARCH_WORD_1)).check(doesNotExist());
    }



    @Test
    public void deleteAllAndCheckDeleted() {
        onView(withId(R.id.searchFragment_searchFieldEditText)).perform(typeText(RECENT_SEARCH_TEST_SEARCH_WORD_1),pressImeActionButton());
        onView(withId(R.id.action_search)).perform(click());
        onView(withId(R.id.searchFragment_deleteTextView)).perform(click());
        onView(withId(100001)).check(doesNotExist());
    }



}
