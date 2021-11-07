package com.plim.kati_app.kati;

import android.app.Application;

import com.kakao.sdk.common.KakaoSdk;

public class KatiApplication extends Application{
    private static KatiApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance=this;
        KakaoSdk.init(this,"9dba46c94bc6ff6476b93e0bbe4494ab");
    }
}
