package com.plim.kati_app.kati.domain.nnew.foodDetail;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.plim.kati_app.R;
import com.plim.kati_app.jshCrossDomain.tech.retrofit.JSHRetrofitTool;
import com.plim.kati_app.kati.crossDomain.domain.model.KatiEntity;
import com.plim.kati_app.kati.crossDomain.domain.view.dialog.KatiDialog;
import com.plim.kati_app.kati.crossDomain.domain.view.fragment.KatiFoodFragment;
import com.plim.kati_app.kati.crossDomain.tech.retrofit.KatiRetrofitTool;
import com.plim.kati_app.kati.crossDomain.tech.retrofit.SimpleRetrofitCallBackImpl;
import com.plim.kati_app.kati.domain.nnew.foodDetail.adapter.ReviewRecyclerAdapter;
import com.plim.kati_app.kati.domain.nnew.review.ReviewActivity;
import com.plim.kati_app.kati.domain.old.model.CreateReviewResponse;
import com.plim.kati_app.kati.domain.old.model.DeleteReviewRequest;
import com.plim.kati_app.kati.domain.old.model.ReadReviewDto;
import com.plim.kati_app.kati.domain.old.model.ReadReviewResponse;
import com.plim.kati_app.kati.domain.old.model.ReadSummaryResponse;
import com.plim.kati_app.kati.domain.old.model.UpdateReviewLikeRequest;
import com.plim.kati_app.kati.domain.old.model.UpdateReviewLikeResponse;

import java.util.List;
import java.util.Vector;

import retrofit2.Response;

public class ReviewFragment extends KatiFoodFragment {

    //associate
    //view
    private Button writeReviewButton;
    private RecyclerView recyclerView;

    private ProgressBar firstProgressBar, secondProgressBar, thirdProgressBar, fourthProgressBar, fifthProgressBar;
    private TextView firstValueTextView, secondValueTextView, thirdValueTextView, fourthValueTextView, fifthValueTextView,
            ratingTextView;
    private RatingBar ratingBar;

    private NestedScrollView nestedScrollView;

    //attribute
    private int currentPageNum = 1;
    private boolean hasNext = false;

    private Vector<ReadReviewResponse> vector;


    private View.OnClickListener updateListener, likeListener, unLikeListener, deleteListener;
    private ReviewRecyclerAdapter adapter;

    public ReviewFragment() {
        this.vector = new Vector<>();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_review;
    }

    @Override
    protected void associateView(View view) {
        this.firstProgressBar = view.findViewById(R.id.reviewSummaryWidget_firstProgressBar);
        this.secondProgressBar = view.findViewById(R.id.reviewSummaryWidget_secondProgressBar);
        this.thirdProgressBar = view.findViewById(R.id.reviewSummaryWidget_thirdProgressBar);
        this.fourthProgressBar = view.findViewById(R.id.reviewSummaryWidget_fourthProgressBar);
        this.fifthProgressBar = view.findViewById(R.id.reviewSummaryWidget_fifthProgressBar);

        this.ratingTextView = view.findViewById(R.id.reviewSummaryWidget_ratingTextView);
        this.ratingBar = view.findViewById(R.id.reviewSummaryWidget_ratingBar);

        this.firstValueTextView = view.findViewById(R.id.reviewSummaryWidget_firstValueTextView);
        this.secondValueTextView = view.findViewById(R.id.reviewSummaryWidget_secondValueTextView);
        this.thirdValueTextView = view.findViewById(R.id.reviewSummaryWidget_thirdValueTextView);
        this.fourthValueTextView = view.findViewById(R.id.reviewSummaryWidget_fourthValueTextView);
        this.fifthValueTextView = view.findViewById(R.id.reviewSummaryWidget_fifthValueTextView);

        this.writeReviewButton = view.findViewById(R.id.reviewSummaryWidget_writeReviewButton);
        this.recyclerView = view.findViewById(R.id.reviewFragment_recyclerView);

        this.nestedScrollView=view.findViewById(R.id.reviewFragment_nestedScrollView);
    }

    @Override
    protected void initializeView() {
        LinearLayoutManager manager=new LinearLayoutManager(this.getContext());
        this.writeReviewButton.setOnClickListener(v -> this.moveToReviewActivity());
        this.recyclerView.setLayoutManager(manager);
        this.recyclerView.setNestedScrollingEnabled(false);

        this.deleteListener = v -> deleteReview(dataset.get(KatiEntity.EKatiData.AUTHORIZATION), (Long) v.getTag());
        this.likeListener = v -> like(dataset.get(KatiEntity.EKatiData.AUTHORIZATION), (Long) v.getTag(), true);
        this.unLikeListener = v -> like(dataset.get(KatiEntity.EKatiData.AUTHORIZATION), (Long) v.getTag(), false);
        this.updateListener = v -> updateReviews((Long) v.getTag());

        this.adapter = new ReviewRecyclerAdapter(this.getActivity(), deleteListener, unLikeListener, likeListener, updateListener);
        this.recyclerView.setAdapter(this.adapter);

        this.nestedScrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (v.getChildAt(v.getChildCount() - 1) != null) {
                if ((scrollY >= (v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight())) &&
                        scrollY > oldScrollY) {
                    loadMore();
                }
            }
        });




    }


    public void moveToReviewActivity() {
        Intent intent = new Intent(this.getActivity(), ReviewActivity.class);
        intent.putExtra("isUpdate", false);
        intent.putExtra("foodId", foodDetailResponse.getFoodId());
//        intent.putExtra("photo", foodDetailResponse.getFoodImageAddress());
//        intent.putExtra("foodName", foodDetailResponse.getFoodName());
//        intent.putExtra("manufacturerName", foodDetailResponse.getManufacturerName());
        this.startActivity(intent);
    }

    public void setProgressBar(ProgressBar progressBar, float percent) {
        progressBar.setMax(100);
        progressBar.setMin(0);
        progressBar.setProgress((int) (percent * 100));
    }


    @Override
    public void foodModelDataUpdated() {
        this.refresh();
    }

    @Override
    protected void summaryDataUpdated() {
        setProgressBar(this.firstProgressBar, (float) readSummaryResponse.getOneCount() / readSummaryResponse.getReviewCount());
        setProgressBar(this.secondProgressBar, (float) readSummaryResponse.getTwoCount() / readSummaryResponse.getReviewCount());
        setProgressBar(this.thirdProgressBar, (float) readSummaryResponse.getThreeCount() / readSummaryResponse.getReviewCount());
        setProgressBar(this.fourthProgressBar, (float) readSummaryResponse.getFourCount() / readSummaryResponse.getReviewCount());
        setProgressBar(this.fifthProgressBar, (float) readSummaryResponse.getFiveCount() / readSummaryResponse.getReviewCount());

        this.firstValueTextView.setText(String.valueOf(readSummaryResponse.getOneCount()));
        this.secondValueTextView.setText(String.valueOf(readSummaryResponse.getTwoCount()));
        this.thirdValueTextView.setText(String.valueOf(readSummaryResponse.getThreeCount()));
        this.fourthValueTextView.setText(String.valueOf(readSummaryResponse.getFourCount()));
        this.fifthValueTextView.setText(String.valueOf(readSummaryResponse.getFiveCount()));

        this.ratingTextView.setText(String.valueOf(readSummaryResponse.getAvgRating()));
        this.ratingBar.setRating(readSummaryResponse.getAvgRating());
    }

    private class ReadReviewCallback extends SimpleRetrofitCallBackImpl<ReadReviewDto> {

        public ReadReviewCallback(Activity activity) {
            super(activity);
        }

        @Override
        public void onSuccessResponse(Response<ReadReviewDto> response) {
            onReadReviewResponse(response);
        }

    }

    private class ReadUserReviewCallback extends SimpleLoginRetrofitCallBack<ReadReviewDto> {

        public ReadUserReviewCallback(Activity activity) {
            super(activity);
        }

        @Override
        public void onSuccessResponse(Response<ReadReviewDto> response) {
            onReadReviewResponse(response);
        }

    }

    private class LikeReviewCallback extends SimpleLoginRetrofitCallBack<UpdateReviewLikeResponse> {
        private boolean likeCheck;

        public LikeReviewCallback(Activity activity, boolean likeCheck) {
            super(activity);
            this.likeCheck = likeCheck;
        }

        @Override
        public void onSuccessResponse(Response<UpdateReviewLikeResponse> response) {
            refresh();
        }
    }

    private class DeleteReviewCallback extends SimpleLoginRetrofitCallBack<CreateReviewResponse> {

        public DeleteReviewCallback(Activity activity) {
            super(activity);
        }

        @Override
        public void onSuccessResponse(Response<CreateReviewResponse> response) {
            refresh();
        }
    }

    private void onReadReviewResponse(Response<ReadReviewDto> response) {
        ReadReviewDto reviewDto = response.body();

        ReadSummaryResponse readSummaryResponse = reviewDto.getReadSummaryResponse();
        int findReviewPageCount = readSummaryResponse.getReviewPageCount();
        hasNext = (currentPageNum < findReviewPageCount);
        foodModel.getReadSummaryResponse().setValue(readSummaryResponse);
        saveReadSummary();

        List<ReadReviewResponse> reviewList = reviewDto.getReadReviewResponse();
        vector.addAll(reviewList);
        adapter.setItems(vector);
    }


    private void refresh() {
        this.currentPageNum=1;
        this.vector.clear();
        this.load();
    }

    private void loadMore() {
        this.currentPageNum++;
        this.load();
    }



    private void deleteReview(String token, Long reviewId) {
        DeleteReviewRequest request = new DeleteReviewRequest(reviewId);
        KatiRetrofitTool.getAPIWithAuthorizationToken(token).deleteReview(request).enqueue(JSHRetrofitTool.getCallback(new DeleteReviewCallback(getActivity())));
    }

    private void like(String token, Long reviewId, boolean likeCheck) {
        UpdateReviewLikeRequest request = new UpdateReviewLikeRequest();
        request.setReviewId(reviewId);
        request.setLikeCheck(likeCheck);

        KatiRetrofitTool.getAPIWithAuthorizationToken(token).likeReview(request).enqueue(JSHRetrofitTool.getCallback(new LikeReviewCallback(this.getActivity(), likeCheck)));
    }

    private void updateReviews(Long reviewId) {
        //리뷰수정
        for(ReadReviewResponse review: this.vector){
            if(review.getReviewId().equals(reviewId)){
                Intent intent = new Intent(this.getActivity(), ReviewActivity.class);
                intent.putExtra("reviewId", reviewId);
                intent.putExtra("foodId", this.foodDetailResponse.getFoodId());
                intent.putExtra("isUpdate", true);
                intent.putExtra("ratingScore",review.getReviewRating());
                intent.putExtra("reviewText",review.getReviewDescription());
                startActivity(intent);
            }
        }

    }

    private void load() {
        if (!this.dataset.get(KatiEntity.EKatiData.AUTHORIZATION).equals(KatiEntity.EKatiData.NULL.name())) {
            this.getReviews(this.dataset.get(KatiEntity.EKatiData.AUTHORIZATION));
        } else this.getReviews();
    }

    private void getReviews(String token) {
        KatiRetrofitTool.getAPIWithAuthorizationToken(token).readReviewByUser(this.foodDetailResponse.getFoodId(), this.currentPageNum)
                .enqueue(JSHRetrofitTool.getCallback(new ReadUserReviewCallback(this.getActivity())));
    }

    private void getReviews() {
        KatiRetrofitTool.getAPI().readReview(this.foodDetailResponse.getFoodId(), this.currentPageNum)
                .enqueue(JSHRetrofitTool.getCallback(new ReadReviewCallback(this.getActivity())));
    }

}