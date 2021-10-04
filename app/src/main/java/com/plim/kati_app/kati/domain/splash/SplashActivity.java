package com.plim.kati_app.kati.domain.splash;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.plim.kati_app.R;
import com.plim.kati_app.kati.domain.foodDetail.FoodDetailActivity;
import com.plim.kati_app.kati.domain.main.MainActivity;

import static com.plim.kati_app.kati.crossDomain.domain.model.Constant.DETAIL_PRODUCT_INFO_TABLE_FRAGMENT_FOOD_ID_EXTRA;
import static com.plim.kati_app.kati.crossDomain.domain.model.Constant.NEW_DETAIL_ACTIVITY_EXTRA_FOOD_ID;
import static com.plim.kati_app.kati.crossDomain.domain.model.Constant.NEW_DETAIL_ACTIVITY_EXTRA_IS_AD;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE|
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE|
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION|
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION|
                        View.SYSTEM_UI_FLAG_FULLSCREEN);

        new Thread(() -> {
            try {Thread.sleep(300); }
            catch (InterruptedException e) { e.printStackTrace(); }
            Intent intent = getIntent();
            if(Intent.ACTION_VIEW.equals(intent.getAction())){
                Intent intentKakao = new Intent(this, FoodDetailActivity.class);
                intentKakao.putExtra(NEW_DETAIL_ACTIVITY_EXTRA_FOOD_ID, Long.parseLong(intent.getData().getQueryParameter("food_id")));
                intentKakao.putExtra(NEW_DETAIL_ACTIVITY_EXTRA_IS_AD, false);
                this.startActivity(new Intent(this, MainActivity.class));
                this.startActivity(intentKakao);
            }else{
                this.startActivity(new Intent(this, MainActivity.class));
            }

        }).start();
    }
}