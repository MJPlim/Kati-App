package com.plim.kati_app.testUtil;

import android.app.Activity;
import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;

import com.plim.kati_app.R;

import org.junit.After;
import org.junit.Before;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@LargeTest
public abstract class KatiTestClass<T extends Activity> {
    private ActivityScenario<T> scenario;

    @Before
    public void before() throws InterruptedException {
        this.scenario= ActivityScenario.launch(this.getTargetActivityClass()) ;
        Application application = ApplicationProvider.getApplicationContext();
        SharedPreferences p = PreferenceManager.getDefaultSharedPreferences(application);
        this.beforeTest();
    }

    @After
    public void after() throws InterruptedException {
        this.afterTest();
        this.scenario.close();
    }

    protected abstract Class getTargetActivityClass();
    protected abstract void beforeTest();
    protected void afterTest(){};

    protected void clickView(int viewId){
        onView(withId(viewId)).perform(click());
    }
    protected void replaceViewText(int viewId, String targetText){
        onView(withId(viewId)).perform(replaceText(targetText));
    }

    protected void checkTextExists(String targetText){
        onView(withText(targetText)).check(matches(isDisplayed()));
    }
    protected void checkTextNotExists(String targetText){
        onView(withText(targetText)).check(doesNotExist());
    }

    protected void checkViewExists(int viewId){
        onView(withId(viewId)).perform(click());
    }

    protected void clickText(String targetText){
        onView(withText(targetText)).perform(click());
    }

    protected void clickDialogOption(String targetText) throws UiObjectNotFoundException {
        UiDevice uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        UiObject button = uiDevice.findObject(new UiSelector().text(targetText));
        if (button.exists() && button.isEnabled()) {
            button.click();
        }
    }
}
