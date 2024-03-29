package com.plim.kati_app.kati.crossDomain.tech.retrofit;

import android.app.DownloadManager;

import com.plim.kati_app.kati.domain.findId.model.FindEmailRequest;
import com.plim.kati_app.kati.domain.findId.model.FindEmailResponse;
import com.plim.kati_app.kati.domain.login.model.SocialLoginRequest;
import com.plim.kati_app.kati.domain.main.favorite.model.UserFavoriteResponse;
import com.plim.kati_app.kati.domain.main.myKati.allergy.model.CreateUserAllergyRequest;
import com.plim.kati_app.kati.domain.main.myKati.allergy.model.CreateUserAllergyResponse;
import com.plim.kati_app.kati.domain.main.myKati.allergy.model.ReadUserAllergyResponse;
import com.plim.kati_app.kati.domain.main.myKati.model.UserSummaryResponse;
import com.plim.kati_app.kati.domain.main.myKati.myInfoEdit.model.GetSecondEmailResponse;
import com.plim.kati_app.kati.domain.main.myKati.review.model.ReadReviewResponse;
import com.plim.kati_app.kati.domain.main.myKati.review.model.ReadUserReviewResponse;
import com.plim.kati_app.kati.domain.main.search.model.AdvertisementDetailResponse;
import com.plim.kati_app.kati.domain.setRestoreEmail.model.SetSecondEmailRequest;
import com.plim.kati_app.kati.domain.setRestoreEmail.model.SetSecondEmailResponse;
import com.plim.kati_app.kati.domain.editPassword.model.ModifyPasswordRequest;
import com.plim.kati_app.kati.domain.editPassword.model.ModifyPasswordResponse;
import com.plim.kati_app.kati.domain.login.model.LoginResponse;
import com.plim.kati_app.kati.domain.findPassword.model.FindPasswordRequest;
import com.plim.kati_app.kati.domain.findPassword.model.FindPasswordResponse;
import com.plim.kati_app.kati.domain.signUp.model.SignUpRequest;
import com.plim.kati_app.kati.domain.signUp.model.SignUpResponse;
import com.plim.kati_app.kati.domain.foodDetail.model.FoodDetailResponse;
import com.plim.kati_app.kati.domain.review.model.CreateAndUpdateReviewRequest;
import com.plim.kati_app.kati.domain.review.model.CreateReviewResponse;
import com.plim.kati_app.kati.domain.main.search.model.DeleteReviewRequest;
import com.plim.kati_app.kati.domain.main.search.model.FindFoodByBarcodeRequest;
import com.plim.kati_app.kati.domain.main.search.model.ReadReviewDto;
import com.plim.kati_app.kati.domain.main.search.model.ReadReviewIdResponse;
import com.plim.kati_app.kati.domain.main.search.model.UpdateReviewLikeRequest;
import com.plim.kati_app.kati.domain.main.search.model.UpdateReviewLikeResponse;
import com.plim.kati_app.kati.domain.main.search.model.AdvertisementResponse;
import com.plim.kati_app.kati.domain.main.search.model.FindFoodBySortingResponse;
import com.plim.kati_app.kati.domain.main.myKati.myInfoEdit.model.UserInfoModifyRequest;
import com.plim.kati_app.kati.domain.main.myKati.myInfoEdit.model.UserInfoResponse;
import com.plim.kati_app.kati.domain.main.search.model.ItemRankingResponse;
import com.plim.kati_app.kati.domain.signOut.model.WithdrawRequest;
import com.plim.kati_app.kati.domain.signOut.model.WithdrawResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;



public interface KatiRestAPI {

    @POST("signup")
    Call<SignUpResponse> signUp(@Body SignUpRequest signUpRequest);

    @POST("api/v1/user/withdraw")
    Call<WithdrawResponse> withdraw(@Body WithdrawRequest user);

    @POST("find-password")
    Call<FindPasswordResponse> findPassword(@Body FindPasswordRequest request);

    @POST("find-email")
    Call<FindEmailResponse> findEmail(@Body FindEmailRequest request);

    @POST("api/v1/user/set-secondEmail")
    Call<SetSecondEmailResponse> setSecondEmail(@Body SetSecondEmailRequest request);

    @POST("api/v1/user/modify-password")
    Call<ModifyPasswordResponse> modifyPassword(@Body ModifyPasswordRequest request);

    @POST("login")
    Call<LoginResponse> login(@Body LoginResponse loginRequest);

    @POST("oauth2-login")
    Call<LoginResponse>socialLogin(@Body SocialLoginRequest request);

    @GET("api/v1/user/user-info")
    Call<UserInfoResponse> getUserInfo();

    @GET("api/v1/user/get-secondEmail")
    Call<GetSecondEmailResponse> getSecondEmail();

    @POST("api/v1/user/modify-user-info")
    Call<UserInfoResponse> modifyUserInfo(@Body UserInfoModifyRequest request);

    //알레르기
    @POST("api/v1/user/createUserAllergy")
    Call<CreateUserAllergyResponse> createUserAllergy(@Body CreateUserAllergyRequest dto);
    @GET("api/v1/user/readUserAllergy")
    Call<ReadUserAllergyResponse> readUserAllergy();



    @GET("api/v1/food/searchFood")
    Call<FindFoodBySortingResponse> getNameFoodListBySorting(
            @Query("pageNo") int pageNo,
            @Query("size") int size,
            @Query("sort") String sortElement,
            @Query("order") String order,
            @Query("foodName") String foodName,
            @Query("allergies") List<String> allergyList
    );

    @GET("api/v1/food/searchFood")
        Call<FindFoodBySortingResponse> getManufacturerFoodListBySorting(
            @Query("pageNo") int pageNo,
            @Query("size") int size,
            @Query("sort") String sortElement,
            @Query("order") String order,
            @Query("manufacturerName") String manufacturerName,
            @Query("allergies") List<String> allergyList
    );

    @GET("api/v1/food/searchFood")
    Call<FindFoodBySortingResponse> getCategoryFood(
            @Query("pageNo") int pageNo,
            @Query("size") int size,
            @Query("sort") String sortElement,
            @Query("order") String order,
            @Query("category") String category,
            @Query("allergies") List<String> allergyList
    );


    @GET("api/v1/food/findFood/foodDetail")
    Call<FoodDetailResponse> getFoodDetailByFoodId(@Query("foodId") Long foodId);

    @POST("api/v1/food/findFood/barcode")
    Call<FoodDetailResponse> findByBarcode(@Body FindFoodByBarcodeRequest request);

    //제품 즐겨찾기
    @GET("api/v1/user/favorite/checkFavorite")
    Call<Boolean> getFavoriteStateForFood(@Query("foodId") Long foodId);

    @POST("api/v1/user/favorite/addFavorite")
    Call<Boolean> addFavoriteFood(@Query("foodId") Long foodId);

    @DELETE("api/v1/user/favorite/deleteFavorite")
    Call<Void> deleteFavoriteFood(@Query("foodId") Long foodId);

    //제품 광고
    @GET("api/v1/advertisement/search")
    Call<List<AdvertisementResponse>> getAdFoodList(@Query("size") Integer size);

    @GET("api/v1/advertisement/{adId}")
    Call<AdvertisementDetailResponse> getAdFoodDetail(@Path("adId") Long adId);



    //제품 리뷰
    @GET("readReview")
    Call<ReadReviewDto> readReview(@Query("foodId") Long foodId, @Query("pageNum") int pageNum);

    @GET("api/v1/user/readReview")
    Call<ReadReviewDto> readReviewByUser(@Query("foodId") Long foodId, @Query("pageNum") int pageNum);

    @POST("api/v1/user/updateReviewLike")
    Call<UpdateReviewLikeResponse> likeReview(@Body UpdateReviewLikeRequest updateReviewLikeRequest);

    @POST("api/v1/user/createReview")
    Call<CreateReviewResponse> createReview(@Body CreateAndUpdateReviewRequest dto);

    @POST("api/v1/user/updateReview")
    Call<CreateReviewResponse> updateReview(@Body CreateAndUpdateReviewRequest dto);

    @POST("api/v1/user/deleteReview")
    Call<CreateReviewResponse> deleteReview(@Body DeleteReviewRequest dto);

    @GET("api/v1/user/readReviewByReviewID")
    Call<ReadReviewIdResponse> readReviewByReviewID(@Query("reviewId") Long reviewId);






    @GET("api/v1/advertisement/foodDetail") Call<FoodDetailResponse> getAdvertisementFoodDetail(@Query("adId") Long adId);
    @GET("api/v1/advertisement/ads") Call<List<AdvertisementResponse>> getAdvertisementFoodList();
    @GET("reviewRanking") Call<List<ItemRankingResponse>> getRankingList();
    @GET("api/v1/user/favorite/list") Call<List<UserFavoriteResponse>> getUserFavorite();
    @GET("api/v1/user/readReviewByUserID") Call<ReadUserReviewResponse<List<ReadReviewResponse>>> getUserReview();
    @GET("api/v1/user/summary") Call<UserSummaryResponse> getUserSummary();


}

