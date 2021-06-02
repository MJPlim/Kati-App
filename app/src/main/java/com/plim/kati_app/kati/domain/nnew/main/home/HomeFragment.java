package com.plim.kati_app.kati.domain.nnew.main.home;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.viewpager2.widget.ViewPager2;

import com.plim.kati_app.R;
import com.plim.kati_app.jshCrossDomain.tech.retrofit.JSHRetrofitTool;
import com.plim.kati_app.kati.crossDomain.domain.model.Constant;
import com.plim.kati_app.kati.crossDomain.domain.model.KatiEntity;
import com.plim.kati_app.kati.crossDomain.domain.view.etc.JSHViewPagerTool;
import com.plim.kati_app.kati.crossDomain.domain.view.etc.YYECategoryItem;
import com.plim.kati_app.kati.crossDomain.domain.view.fragment.KatiViewModelFragment;
import com.plim.kati_app.kati.crossDomain.tech.retrofit.KatiRetrofitTool;
import com.plim.kati_app.kati.crossDomain.tech.retrofit.SimpleRetrofitCallBackImpl;
import com.plim.kati_app.kati.domain.nnew.login.model.LoginResponse;
import com.plim.kati_app.kati.domain.nnew.main.home.advertisement.AdvertisementViewPagerAdapter;

import retrofit2.Response;

public class HomeFragment extends KatiViewModelFragment {

    private ViewPager2 viewPager2;
    private TextView searchText;
    private GridLayout layout;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void associateView(View view) {
        this.viewPager2 = view.findViewById(R.id.homeFragment_viewPager);
        this.layout = view.findViewById(R.id.homeFragment_category);
        this.searchText = view.findViewById(R.id.homeFragment_searchTextView);
    }

    @Override
    protected void initializeView() {
        this.viewPager2.setAdapter(new AdvertisementViewPagerAdapter());
        JSHViewPagerTool.setAutoSlide(this.viewPager2, 5000);
        JSHViewPagerTool.setEffect(this.viewPager2);

        this.layout.removeAllViews();
        for (Constant.ECategory category : Constant.ECategory.values()) {
            YYECategoryItem item = new YYECategoryItem(getContext());
            item.setText(category.getName());
            item.setImage(getResources().getDrawable(category.getDrawable(), getActivity().getTheme()));
            item.setOnClickListener(v -> this.moveToCategory(category));
            this.layout.addView(item);
        }
        this.searchText.setOnClickListener(v -> {
//            Button button = v.findViewById(R.id.action_search);
//            if (button != null)
//                button.performClick();
            navigateTo(R.id.action_global_searchFragment);
        });



    }

    @Override
    protected void katiEntityUpdated() {
        this.autoLogin();
    }

    private void moveToCategory(Constant.ECategory category) {
        navigateTo(R.id.action_homeFragment_to_categoryFragment);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        this.autoLogout();
        this.save();
        super.onDestroy();
    }

    /**
     * Callback
     */
    private class LoginRequestCallback extends SimpleRetrofitCallBackImpl<LoginResponse> {
        public LoginRequestCallback(Activity activity) {
            super(activity);
        }

        @Override
        public void onSuccessResponse(Response<LoginResponse> response) {
        }

        @Override
        public void onResponse(Response<LoginResponse> response) {
            dataset.put(KatiEntity.EKatiData.AUTHORIZATION, response.headers().get("Authorization"));
        }
    }

    /**
     * method
     */
    private void autoLogin() {
        if (this.dataset.get(KatiEntity.EKatiData.AUTO_LOGIN).equals(KatiEntity.EKatiData.TRUE.name())) {
//            Log.d("디버그 자동로그인", "설정O");
            LoginResponse loginRequest = new LoginResponse(this.dataset.get(KatiEntity.EKatiData.EMAIL), this.dataset.get(KatiEntity.EKatiData.PASSWORD));
            KatiRetrofitTool.getAPIWithNullConverter().login(loginRequest).enqueue(JSHRetrofitTool.getCallback(new LoginRequestCallback(this.getActivity())));
        }
    }

    private void autoLogout() {
        this.dataset.put(KatiEntity.EKatiData.AUTHORIZATION, KatiEntity.EKatiData.NULL.name());

        if (this.dataset.get(KatiEntity.EKatiData.AUTO_LOGIN).equals(KatiEntity.EKatiData.FALSE.name())) {
//            Log.d("디버그 자동로그아웃", "시작");
            this.dataset.put(KatiEntity.EKatiData.EMAIL, KatiEntity.EKatiData.NULL.name());
            this.dataset.put(KatiEntity.EKatiData.PASSWORD, KatiEntity.EKatiData.NULL.name());
        }
    }

}