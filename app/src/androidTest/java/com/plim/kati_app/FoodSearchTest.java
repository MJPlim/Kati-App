package com.plim.kati_app;


import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.PerformException;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;

import com.plim.kati_app.kati.domain.main.MainActivity;
import com.plim.kati_app.kati.domain.main.search.SearchResultFragment;
import com.plim.kati_app.kati.domain.main.search.adapter.FoodRecyclerAdapter;
import com.plim.kati_app.kati.domain.main.search.adapter.FoodViewHolder;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.pressImeActionButton;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class FoodSearchTest {
    private String TEST_ITEM="at";



    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void init(){

        try {
            onView(withId(R.id.action_search)).perform(click());
        } catch (PerformException e) {
            onView(withId(R.id.myKatiFragment_loginButton)).perform(click());
            onView(withId(R.id.loginActivity_emaiEditText)).perform(typeText("sh199919@gmail.com"), closeSoftKeyboard());
            onView(withId(R.id.loginActivity_passwordEditText)).perform(typeText("1234567"), closeSoftKeyboard());
            onView(withId(R.id.loginActivity_loginButton)).perform(click());
            onView(withId(R.id.action_search)).perform(click());
        }
    }

    @Test
    public void FoodSearchTest(){
        onView(withId(R.id.searchFragment_searchFieldEditText)).perform(typeText(TEST_ITEM), pressImeActionButton());
        onView(withId(R.id.searchResultFragment_resultRecyclerView)).check(matches(isDisplayed()));
    }


}
