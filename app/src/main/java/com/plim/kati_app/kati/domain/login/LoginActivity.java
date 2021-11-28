package com.plim.kati_app.kati.domain.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.kakao.sdk.user.model.Account;

import androidx.fragment.app.Fragment;
import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.OAuthLoginHandler;
import com.google.gson.Gson;
import com.kakao.sdk.user.UserApiClient;
import com.plim.kati_app.R;
import com.plim.kati_app.jshCrossDomain.tech.retrofit.JSHRetrofitCallback;
import com.plim.kati_app.jshCrossDomain.tech.retrofit.JSHRetrofitTool;
import com.plim.kati_app.kati.crossDomain.domain.model.KatiEntity;
import com.plim.kati_app.kati.crossDomain.domain.view.activity.KatiHasTitleActivity;
import com.plim.kati_app.kati.crossDomain.domain.view.activity.KatiLoginCheckViewModelActivity;
import com.plim.kati_app.kati.crossDomain.domain.view.dialog.KatiDialog;
import com.plim.kati_app.kati.crossDomain.tech.retrofit.KatiRetrofitTool;
import com.plim.kati_app.kati.crossDomain.tech.retrofit.SimpleRetrofitCallBackImpl;
import com.plim.kati_app.kati.domain.findId.FindIdActivity;
import com.plim.kati_app.kati.domain.findPassword.FindPasswordActivity;
import com.plim.kati_app.kati.domain.login.model.LoginResponse;
import com.plim.kati_app.kati.domain.login.model.NaverConnectResponse;
import com.plim.kati_app.kati.domain.login.model.NaverLoginResponse;
import com.plim.kati_app.kati.domain.login.model.SocialLoginRequest;
import com.plim.kati_app.kati.domain.signUp.SignUpActivity;
import com.plim.kati_app.kati.domain.signUp.model.SignUpRequest;
import com.plim.kati_app.kati.domain.signUp.model.SignUpResponse;
import com.plim.kati_app.kati.domain.signUp.model.SocialLoginModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Vector;

import retrofit2.Response;

import static com.plim.kati_app.kati.crossDomain.domain.model.Constant.FOOD_SEARCH_RESULT_LIST_FRAGMENT_FAILURE_DIALOG_TITLE;
import static com.plim.kati_app.kati.crossDomain.domain.model.Constant.JSONOBJECT_EMAIL_MESSAGE;
import static com.plim.kati_app.kati.crossDomain.domain.model.Constant.JSONOBJECT_ERROR_MESSAGE;
import static com.plim.kati_app.kati.crossDomain.domain.model.Constant.JSONOBJECT_MESSAGE;
import static com.plim.kati_app.kati.crossDomain.domain.model.Constant.JSONOBJECT_NAME_MESSAGE;
import static com.plim.kati_app.kati.crossDomain.domain.model.Constant.JSONOBJECT_PASSWORD_MESSAGE;
import static com.plim.kati_app.kati.crossDomain.domain.model.Constant.SIGN_UP_CONGRAT_MESSAGE;
import static com.plim.kati_app.kati.crossDomain.domain.model.Constant.SIGN_UP_CONGRAT_TITLE_PREFIX;
import static com.plim.kati_app.kati.crossDomain.domain.model.Constant.SIGN_UP_CONGRAT_TITLE_SUFFIX;

/**
 * 로그인 액티비티.
 */
public class LoginActivity extends KatiLoginCheckViewModelActivity {


    //social Login
    private OAuthLogin mOAuthLoginModule;
    private String clientId = "PnUl6zFN9pcXEHx8qlad";
    private String secret = "M0x9ds9WLh";

    //associate
    //view
    private TextView findId, findPw, signIn;
    private Button kakaoLoginButton, naverLoginButton;
    private EditText emailAddress, password;
    private Button loginButton;
    private CheckBox autologinCheckBox;
    private Vector<KatiDialog> dialogs;

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        if (!(emailAddress.length() == 0) && !(password.length() == 0)) {
            this.loginButton.setEnabled(true);
        } else {
            this.loginButton.setEnabled(false);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        for (KatiDialog dialog : dialogs)
            dialog.dismiss();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new BubbleEffector(this.findViewById(R.id.background)).start();
    }

    @Override
    protected void associateView() {
        this.initSocialLogin();
        View view = this.findViewById(R.id.constraintLayout);
        this.dialogs = new Vector<>();
        this.emailAddress = view.findViewById(R.id.loginActivity_emaiEditText);
        this.password = view.findViewById(R.id.loginActivity_passwordEditText);
        this.loginButton = view.findViewById(R.id.loginActivity_loginButton);
        this.autologinCheckBox = view.findViewById(R.id.loginActivity_autoLoginCheckbox);
        this.findId = view.findViewById(R.id.loginActivity_findIdTextView);
        this.findPw = view.findViewById(R.id.loginActivity_findPWTextView);
        this.signIn = view.findViewById(R.id.loginActivity_signInTextView);
        this.naverLoginButton = this.findViewById(R.id.loginActivity_naverLoginButton);
        this.kakaoLoginButton = this.findViewById(R.id.loginActivity_kakoLoginButton);
    }

    private void initSocialLogin() {
        //init naverLoginModule
        this.mOAuthLoginModule = OAuthLogin.getInstance();
        this.mOAuthLoginModule.init(this, this.clientId, this.secret, "카티");
    }

    @Override
    protected void initializeView() {
        this.findId.setOnClickListener(v -> this.startActivity(new Intent(this, FindIdActivity.class)));
        this.findPw.setOnClickListener(v -> this.startActivity(new Intent(this, FindPasswordActivity.class)));
        this.signIn.setOnClickListener(v -> this.startActivity(new Intent(this, SignUpActivity.class)));
        this.autologinCheckBox.setOnClickListener((v -> this.setAutoLogin()));
        this.loginButton.setOnClickListener(v -> this.login());
        this.naverLoginButton.setOnClickListener(v -> this.naverLogin());
        this.kakaoLoginButton.setOnClickListener(v -> this.kakaoLogin());
    }


    @Override
    protected boolean isLoginNeeded() {
        return false;
    }

    @Override
    protected void katiEntityUpdatedAndLogin() {}

    @Override
    protected void katiEntityUpdatedAndNoLogin() {}


    /**
     * callback
     */
    private class LoginRequestCallback implements JSHRetrofitCallback<LoginResponse> {
        @Override
        public void onSuccessResponse(Response<LoginResponse> response) {
            saveLoginData(response.headers().get("Authorization"));
            onBackPressed();
        }

        @Override
        public void onFailResponse(Response<LoginResponse> response) {
                dialogs.add(KatiDialog.simplerAlertDialogString(LoginActivity.this,
                        getString(R.string.login_failed_dialog), getString(R.string.login_failed_content_dialog),
                        (dialog, which) -> {
                            emailAddress.setText("");
                            password.setText("");
                        }
                ));
        }

        @Override
        public void onConnectionFail(Throwable t) {
            Log.e(FOOD_SEARCH_RESULT_LIST_FRAGMENT_FAILURE_DIALOG_TITLE, t.getMessage());
        }
    }


    private class NaverLoginHandler extends OAuthLoginHandler {
        @Override
        public void run(boolean success) {
            if (success) {
                String at = mOAuthLoginModule.getAccessToken(LoginActivity.this);
                socialLogin(at,"NAVER");
            } else {
                String errorCode = mOAuthLoginModule.getLastErrorCode(LoginActivity.this).getCode();
                String errorDesc = mOAuthLoginModule.getLastErrorDesc(LoginActivity.this);
                Toast.makeText(LoginActivity.this, "errorCode:" + errorCode
                        + ", errorDesc:" + errorDesc, Toast.LENGTH_SHORT).show();
            }
        }

    }




    /**
     * method
     */

    private void login() {
        this.login(this.emailAddress.getText().toString(), this.password.getText().toString());
    }
    private void login(String email, String password) {
        LoginResponse loginRequest = new LoginResponse(email, password);
        KatiRetrofitTool.getAPIWithNullConverter().login(loginRequest).enqueue(JSHRetrofitTool.getCallback(new LoginRequestCallback()));
    }


    private void socialLogin(String accessToken,String loginType){
        SocialLoginRequest request= SocialLoginRequest.builder().accessToken(accessToken).loginType(loginType).build();
        socialLogin(request);
    }
    private void socialLogin(SocialLoginRequest request) {
        Log.i(request.getLoginType(),request.getAccessToken());
        KatiRetrofitTool.getAPIWithNullConverter().socialLogin(request).enqueue(JSHRetrofitTool.getCallback(new LoginRequestCallback()));
    }



    private void naverLogin() {
        this.mOAuthLoginModule.startOauthLoginActivity(this, new NaverLoginHandler());
    }

    private void kakaoLogin() {
        UserApiClient.getInstance().loginWithKakaoAccount(this, (token, throwable) -> {
                    if (throwable != null) {
                        Log.e("카카오 로그인", "로그인 실패");
                        throwable.printStackTrace();
                    } else if (token != null) {
                        socialLogin(token.getAccessToken(),"KAKAO");
                    }
                    return null;
                }
        );
    }

    private void setAutoLogin() {
        String autoLogin = this.autologinCheckBox.isChecked() ? KatiEntity.EKatiData.TRUE.name() : KatiEntity.EKatiData.FALSE.name();
        this.dataset.put(KatiEntity.EKatiData.AUTO_LOGIN, autoLogin);
    }

    public void saveLoginData(String authorization) {
        this.dataset.put(KatiEntity.EKatiData.AUTHORIZATION, authorization);
        this.dataset.put(KatiEntity.EKatiData.EMAIL, emailAddress.getText().toString());
        this.dataset.put(KatiEntity.EKatiData.PASSWORD, password.getText().toString());
        save();
    }
}