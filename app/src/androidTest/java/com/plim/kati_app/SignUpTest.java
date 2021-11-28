package com.plim.kati_app;

import android.app.Dialog;

import androidx.test.uiautomator.UiObjectNotFoundException;

import com.plim.kati_app.kati.domain.main.MainActivity;
import com.plim.kati_app.kati.domain.signUp.SignUpActivity;
import com.plim.kati_app.testUtil.KatiTestClass;
import com.plim.kati_app.testUtil.TestTool;

import org.junit.Test;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static com.plim.kati_app.testUtil.TestTool.USER_PASSWORD_YEEUN;

public class SignUpTest extends KatiTestClass<MainActivity> {
    public static final String SIGN_UP_EMAIL = "ttt@ttt.ttt.tt";
    public static final String SIGN_UP_EMAIL_PREFIX="tt11";

    @Override
    protected Class getTargetActivityClass() { return MainActivity.class; }

    @Override
    protected void beforeTest() {
        this.clickView(R.id.action_mykati);
        TestTool.sleep();
        this.clickView(R.id.myKatiFragment_signUpButton);

    }

    @Test
    public void signUp() throws InterruptedException, UiObjectNotFoundException {
        TestTool.sleep();
        this.replaceViewText(R.id.signUpFragment_emailEditText,SIGN_UP_EMAIL_PREFIX+SIGN_UP_EMAIL);
        this.replaceViewText(R.id.signUpFragment_passwordEditText,USER_PASSWORD_YEEUN);
        this.replaceViewText(R.id.signUpFragment_nickNameEditText,"예은");
        this.clickView(R.id.signUpFragment_submitButton);

        TestTool.sleep();
        TestTool.sleep();
        TestTool.sleep();
        this.clickDialogOption("확인");

        this.checkViewExists(R.id.myKatiFragment_loginButton);

    }


}
