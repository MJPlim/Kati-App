package com.plim.kati_app.testUtil;

import com.plim.kati_app.R;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

public class TestTool {

    public static final String USER_EMAIL_YEEUN="yunyen@mz.co.kr";
    public static final String USER_PASSWORD_YEEUN="123qwea";

    public static final String FAV_TEST_FOOD_NAME = "영주한우 갈비세트2호";

    public static final String USER_DATA_EDIT_TEST_NEW_NAME= "TestNick";
    public static final String USER_DATA_EDIT_TEST_ADDRESS= "ggyeongi-do goyang-si";

    public static final String RECENT_SEARCH_TEST_SEARCH_WORD_1="pot";
    public static final String RECENT_SEARCH_TEST_SEARCH_WORD_2="a";
    public static final String RECENT_SEARCH_TEST_SEARCH_WORD_3="b";

    public static void sleep() throws InterruptedException {
        Thread.sleep(1500);
    }

    public static void doLoginIfNotLogin(){
        onView(withId(R.id.action_mykati)).perform(click());
        onView(withId(R.id.myKatiFragment_loginButton)).perform(click());
        onView(withId(R.id.loginActivity_emaiEditText)).perform(typeText(USER_EMAIL_YEEUN), closeSoftKeyboard());
        onView(withId(R.id.loginActivity_passwordEditText)).perform(typeText(USER_PASSWORD_YEEUN), closeSoftKeyboard());
        onView(withId(R.id.loginActivity_loginButton)).perform(click());
    }
}
