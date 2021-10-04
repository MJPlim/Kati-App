package com.plim.kati_app.kati.domain.foodDetail;

import android.app.Activity;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.kakao.kakaolink.v2.KakaoLinkResponse;
import com.kakao.kakaolink.v2.KakaoLinkService;
import com.kakao.message.template.ButtonObject;
import com.kakao.message.template.ContentObject;
import com.kakao.message.template.FeedTemplate;
import com.kakao.message.template.LinkObject;
import com.kakao.message.template.SocialObject;
import com.kakao.network.ErrorResult;
import com.kakao.network.callback.ResponseCallback;
import com.kakao.util.helper.log.Logger;
import com.plim.kati_app.R;
import com.plim.kati_app.jshCrossDomain.tech.retrofit.JSHRetrofitTool;
import com.plim.kati_app.kati.crossDomain.domain.model.KatiEntity;
import com.plim.kati_app.kati.crossDomain.domain.view.etc.JSHInfoItem;
import com.plim.kati_app.kati.crossDomain.domain.view.etc.JSHToolBar;
import com.plim.kati_app.kati.crossDomain.domain.view.fragment.KatiFoodFragment;
import com.plim.kati_app.kati.crossDomain.tech.retrofit.KatiRetrofitTool;
import com.plim.kati_app.kati.crossDomain.tech.retrofit.SimpleRetrofitCallBackImpl;
import com.plim.kati_app.kati.domain.foodDetail.model.FoodDetailResponse;
import com.plim.kati_app.kati.domain.main.search.model.FindFoodByBarcodeRequest;
import com.varunest.sparkbutton.SparkButton;
import com.varunest.sparkbutton.SparkEventListener;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Response;

import static com.plim.kati_app.kati.crossDomain.domain.model.Constant.DETAIL_PRODUCT_INFO_START_NO;
import static com.plim.kati_app.kati.crossDomain.domain.model.Constant.DETAIL_PRODUCT_INFO_START_NONE;
import static com.plim.kati_app.kati.crossDomain.domain.model.Constant.DETAIL_PRODUCT_INFO_START_NULL;
import static com.plim.kati_app.kati.crossDomain.domain.model.Constant.DETAIL_PRODUCT_INFO_TABLE_FRAGMENT_FOOD_ID_BARCODE;
import static com.plim.kati_app.kati.crossDomain.domain.model.Constant.DETAIL_PRODUCT_INFO_TABLE_FRAGMENT_FOOD_ID_EXTRA;
import static com.plim.kati_app.kati.crossDomain.domain.model.Constant.END_REVIEW;
import static com.plim.kati_app.kati.crossDomain.domain.model.Constant.NEW_DETAIL_ACTIVITY_EXTRA_IS_AD;
import static com.plim.kati_app.kati.crossDomain.domain.model.Constant.START_REVIEW;

public class FoodInfoFragment extends KatiFoodFragment {

    //associate
    //view
    private ImageView foodImageView, starIcon,kakaoIcon;
    private TextView foodNameTextView, ratingTextView, reviewCountTextView;
    private JSHInfoItem materialItem, ingredientItem, allergyItem;
    private SparkButton heartButton;

    private JSHToolBar toolBar;


    // Working Variable
    private boolean isFront = true;
    private boolean isFavorite = false;

    //attribute
    private String barcode;
    private Long foodId;
    private boolean isAd;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_food_info;
    }

    @Override
    protected void associateView(View view) {
        this.toolBar = getActivity().findViewById(R.id.toolbar);
        this.foodImageView = view.findViewById(R.id.foodItemFragment_foodImageView);
        this.starIcon = view.findViewById(R.id.foodItemFragment_starIcon);
        this.kakaoIcon = view.findViewById(R.id.foodItemFragment_kakaotalkIcon);

        this.allergyItem = view.findViewById(R.id.foodItemFragment_allergy);

        this.foodNameTextView = view.findViewById(R.id.foodItemFragment_foodNameTextView);
        this.ratingTextView = view.findViewById(R.id.foodItemFragment_ratingTextView);
        this.reviewCountTextView = view.findViewById(R.id.foodItemFragment_reviewCountTextView);

        this.materialItem = view.findViewById(R.id.foodItemFragment_materialItem);
        this.ingredientItem = view.findViewById(R.id.foodItemFragment_ingredientItem);

        this.heartButton = view.findViewById(R.id.foodItemFragment_heartButton);
    }

    @Override
    protected void katiEntityUpdated() {
        this.toolBar.setToolBarOnClickListener(v -> getActivity().onBackPressed());

        this.barcode = this.getActivity().getIntent().getStringExtra(DETAIL_PRODUCT_INFO_TABLE_FRAGMENT_FOOD_ID_BARCODE);
        this.foodId = this.getActivity().getIntent().getLongExtra(DETAIL_PRODUCT_INFO_TABLE_FRAGMENT_FOOD_ID_EXTRA, 0L);
        this.isAd = this.getActivity().getIntent().getBooleanExtra(NEW_DETAIL_ACTIVITY_EXTRA_IS_AD, false);

        if (this.barcode != null) this.barcodeSearch();
        else this.search();
    }

    @Override
    protected void initializeView() {
        this.foodImageView.setOnClickListener(v -> this.changeProductImage(this.isFront));
        this.kakaoIcon.setOnClickListener(v -> this.kakaolink());
        this.heartButton.setEventListener(new SparkEventListener() {
            @Override
            public void onEvent(ImageView button, boolean buttonState) {
                saveLike();
            }

            @Override
            public void onEventAnimationEnd(ImageView button, boolean buttonState) {
            }

            @Override
            public void onEventAnimationStart(ImageView button, boolean buttonState) {
            }
        });
    }

    @Override
    public void foodModelDataUpdated() {
        this.toolBar.setToolBarTitle(this.foodDetailResponse.getFoodName());
        if (!this.dataset.get(KatiEntity.EKatiData.AUTHORIZATION).equals(KatiEntity.EKatiData.NULL.name())) {
            String token = this.dataset.get(KatiEntity.EKatiData.AUTHORIZATION);
            this.foodId = this.foodDetailResponse.getFoodId();
            this.checkFavorite(token);
            this.heartButton.setVisibility(View.VISIBLE);
        } else {
            this.heartButton.setVisibility(View.GONE);
        }
        this.changeImage(this.foodDetailResponse.getFoodImageAddress());
        this.changeImage(this.foodModel.getFoodDetailResponse().getValue().getFoodImageAddress());
        this.foodNameTextView.setText(this.foodDetailResponse.getFoodName());
        this.ratingTextView.setText(String.valueOf(this.foodDetailResponse.getReviewRate()));
        this.reviewCountTextView.setText(START_REVIEW + this.foodDetailResponse.getReviewCount() + END_REVIEW);

        this.materialItem.setContentText(this.splitString(",", this.foodDetailResponse.getMaterials()));
        this.ingredientItem.setContentText(this.splitString("`", this.foodDetailResponse.getNutrient()));
        this.allergyItem.setContentText(this.splitString(" ", this.foodDetailResponse.getAllergyMaterials()));
    }


    @Override
    protected void summaryDataUpdated() {
    }


    private class FoodDetailRequestCallback extends SimpleRetrofitCallBackImpl<FoodDetailResponse> {
        public FoodDetailRequestCallback(Activity activity) {
            super(activity);
        }

        @Override
        public void onSuccessResponse(Response<FoodDetailResponse> response) {
            foodDetailResponse = response.body();
            saveFoodDetail();
        }

    }

    /**
     * callback
     */
    private class AddFavoriteCallBack extends SimpleLoginRetrofitCallBack<Boolean> {
        public AddFavoriteCallBack(Activity activity) {
            super(activity);
        }

        @Override
        public void onSuccessResponse(Response<Boolean> response) {
            setIsFavorite(true);
        }
    }

    private class DeleteFavoriteCallBack extends SimpleLoginRetrofitCallBack<Void> {
        public DeleteFavoriteCallBack(Activity activity) {
            super(activity);
        }

        @Override
        public void onSuccessResponse(Response<Void> response) {
            setIsFavorite(false);
        }
    }

    private class CheckFavoriteCallBack extends SimpleLoginRetrofitCallBack<Boolean> {
        public CheckFavoriteCallBack(Activity activity) {
            super(activity);
        }

        @Override
        public void onSuccessResponse(Response<Boolean> response) {
            setIsFavorite(response.body().booleanValue());
        }
    }

    /**
     * method
     */
    private void search() {
        if (!isAd) {
            KatiRetrofitTool.getAPI().getFoodDetailByFoodId(this.foodId).enqueue(JSHRetrofitTool.getCallback(new FoodDetailRequestCallback(this.getActivity())));
        } else {
            KatiRetrofitTool.getAPI().getAdFoodDetail(this.foodId).enqueue(JSHRetrofitTool.getCallback(new FoodDetailRequestCallback(this.getActivity())));
        }
    }

    private void barcodeSearch() {
        KatiRetrofitTool.getAPI().findByBarcode(new FindFoodByBarcodeRequest(this.barcode)).enqueue(JSHRetrofitTool.getCallback(new FoodDetailRequestCallback(this.getActivity())));
    }

    private void checkFavorite(String token) {
        KatiRetrofitTool.getAPIWithAuthorizationToken(token).getFavoriteStateForFood(this.foodId).enqueue(JSHRetrofitTool.getCallback(new CheckFavoriteCallBack(this.getActivity())));
    }

    private void saveLike() {
        if (!this.dataset.get(KatiEntity.EKatiData.AUTHORIZATION).equals(KatiEntity.EKatiData.NULL.name())) {
            String token = this.dataset.get(KatiEntity.EKatiData.AUTHORIZATION);
            if (!isFavorite) {
                KatiRetrofitTool.getAPIWithAuthorizationToken(token).addFavoriteFood(this.foodId).enqueue(JSHRetrofitTool.getCallback(new AddFavoriteCallBack(this.getActivity())));
            } else {
                KatiRetrofitTool.getAPIWithAuthorizationToken(token).deleteFavoriteFood(this.foodId).enqueue(JSHRetrofitTool.getCallback(new DeleteFavoriteCallBack(this.getActivity())));
            }
        }
    }

    private String splitString(String splitChar, String string) {
        if (string.startsWith(DETAIL_PRODUCT_INFO_START_NULL)||string.startsWith(DETAIL_PRODUCT_INFO_START_NONE)||string.startsWith(DETAIL_PRODUCT_INFO_START_NO))  return null;
        String[] array = string.split(splitChar);
        StringBuilder builder = new StringBuilder();
        boolean inBig = false, inMiddle = false, inSmall = false, inMiddleMiddle = false;
        for (String value : array) {
            if (!value.startsWith(DETAIL_PRODUCT_INFO_START_NULL)&&!value.startsWith("_")) {

                if (value.indexOf('[') != -1) inBig = true;
                if (value.indexOf('<') != -1) inMiddle = true;
                if (value.indexOf('(') != -1) inSmall = true;
                if (value.indexOf('{') != -1) inMiddleMiddle = true;

                if (inBig && value.indexOf(']') != -1) inBig = false;
                if (inMiddle && value.indexOf('>') != -1) inMiddle = false;
                if (inSmall && value.indexOf(')') != -1) inSmall = false;
                if (inSmall && value.indexOf('}') != -1) inMiddleMiddle = false;

                if (inBig || inMiddle || inSmall || inMiddleMiddle) {
                    builder.append(value);
                    builder.append(splitChar);

                } else {
                    builder.append(value);
                    builder.append('\n');
                    builder.append('\n');
                }
            }
        }
        return builder.toString();
    }

    private void changeProductImage(boolean isFront) {
        this.isFront = !isFront;
        if (!this.isFront) {
            this.changeImage(this.foodModel.getFoodDetailResponse().getValue().getFoodImageAddress());
        } else {
            this.changeImage(this.foodModel.getFoodDetailResponse().getValue().getFoodMeteImageAddress());
        }
    }

    public void changeImage(String address) {
        this.foodImageView.setImageTintList(null);
        if (address != null)
            Glide.with(getContext()).load(address).fitCenter().transform(new CenterCrop()).into(this.foodImageView);
    }

    private void setIsFavorite(boolean flag) {
        this.isFavorite = flag;
        this.heartButton.pressOnTouch(flag);
        this.heartButton.setChecked(flag);
    }
    //카카오링크를 통한 공유 메서드
    public void kakaolink() {
        FeedTemplate params = FeedTemplate
                .newBuilder(ContentObject.newBuilder(this.foodDetailResponse.getFoodName(),
                        this.foodDetailResponse.getFoodImageAddress(),
                        LinkObject.newBuilder().setWebUrl("https://www.naver.com")   //링크는 카카오 developer 에서 등록한 링크만 사용가능하다 아니면 무반응
                                .setMobileWebUrl("https://www.naver.com").build())
                        .setDescrption(this.foodDetailResponse.getManufacturerName())
                        .build())
                .setSocial(SocialObject.newBuilder().setViewCount(this.foodDetailResponse.getViewCount().intValue()).setCommentCount(this.foodDetailResponse.getReviewCount())
                        .build())
                .addButton(new ButtonObject("Kati 앱에서 보기", LinkObject.newBuilder()
                        .setWebUrl("https://www.naver.com")
                        .setMobileWebUrl("https://www.naver.com")
                        .setAndroidExecutionParams("food_id="+this.foodDetailResponse.getFoodId())
                        .build()))
                .build();
        KakaoLinkService.getInstance().sendDefault(getContext(), params, new ResponseCallback<KakaoLinkResponse>() {
            @Override
            public void onFailure(ErrorResult errorResult) {
                //실패하면 할일
                Toast.makeText(getContext(),"공유에 실패하였습니다",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(KakaoLinkResponse result) {
                //성공하면 할일
            }
        });
    }




}