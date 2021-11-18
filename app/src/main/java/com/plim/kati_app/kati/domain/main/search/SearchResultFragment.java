package com.plim.kati_app.kati.domain.main.search;

import android.app.Activity;

import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;


import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.plim.kati_app.R;
import com.plim.kati_app.jshCrossDomain.tech.retrofit.JSHRetrofitTool;
import com.plim.kati_app.kati.crossDomain.domain.model.Constant;
import com.plim.kati_app.kati.crossDomain.domain.model.KatiEntity;
import com.plim.kati_app.kati.crossDomain.domain.view.fragment.KatiSearchFragment;
import com.plim.kati_app.kati.crossDomain.tech.retrofit.KatiRetrofitTool;
import com.plim.kati_app.kati.crossDomain.tech.retrofit.SimpleRetrofitCallBackImpl;
import com.plim.kati_app.kati.domain.foodDetail.FoodDetailActivity;
import com.plim.kati_app.kati.domain.main.myKati.allergy.model.ReadUserAllergyResponse;
import com.plim.kati_app.kati.domain.main.search.adapter.AdRecyclerAdapter;
import com.plim.kati_app.kati.domain.main.search.adapter.FoodRecyclerAdapter;
import com.plim.kati_app.kati.domain.main.search.model.AdvertisementResponse;
import com.plim.kati_app.kati.domain.main.search.model.FindFoodBySortingResponse;
import com.plim.kati_app.kati.domain.main.search.model.FoodResponse;

import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import retrofit2.Response;

import static com.plim.kati_app.kati.crossDomain.domain.model.Constant.DETAIL_PRODUCT_INFO_TABLE_FRAGMENT_FOOD_ID_EXTRA;
import static com.plim.kati_app.kati.crossDomain.domain.model.Constant.NEW_DETAIL_ACTIVITY_EXTRA_IS_AD;

public class SearchResultFragment extends KatiSearchFragment {

    //associate
    //view
    private EditText searchFieldEditText;
    private ImageView deleteIcon, orderButton;
    private ChipGroup elementChipGroup;
    private Chip rankingChip, manufacturerChip, reviewCountChip;
    private RecyclerView adRecyclerView, resultRecyclerView;
    private NestedScrollView nestedScrollView;
    private Button allergyFilterButton;

    //adapter
    private FoodRecyclerAdapter foodRecyclerAdapter;
    private AdRecyclerAdapter adRecyclerAdapter;

    //model
    private Vector<FoodResponse> vector;

    //working Variable
    private boolean isLoadingMore = false;
    private boolean hasNext = true;

    public SearchResultFragment() {
        this.vector = new Vector<>();
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_search_result;
    }

    @Override
    protected void associateView(View view) {
        this.searchFieldEditText = view.findViewById(R.id.searchResultFragment_searchFieldEditText);

        this.orderButton =view.findViewById(R.id.searchResultFragment_assendOrDesend);
        this.deleteIcon = view.findViewById(R.id.searchResultFragment_deleteIcon);

        this.elementChipGroup=view.findViewById(R.id.searchResultFragment_elementChipGroup);

        this.rankingChip = view.findViewById(R.id.searchResultFragment_rankingChip);
        this.manufacturerChip = view.findViewById(R.id.searchResultFragment_manufacturerChip);
        this.reviewCountChip = view.findViewById(R.id.searchResultFragment_reviewCountChip);

        this.adRecyclerView = view.findViewById(R.id.searchResultFragment_adRecyclerView);
        this.resultRecyclerView = view.findViewById(R.id.searchResultFragment_resultRecyclerView);

        this.nestedScrollView = view.findViewById(R.id.searchResultFragment_nestedScrollView);
        this.allergyFilterButton = view.findViewById(R.id.searchResultFragment_allergyFilterButton);
    }

    @Override
    protected void initializeView() {
        this.elementChipGroup.setSelectionRequired(true);

        this.adRecyclerAdapter = new AdRecyclerAdapter(this.getActivity(), v -> this.doClickOnAdItem(v));
        this.foodRecyclerAdapter = new FoodRecyclerAdapter(this.getActivity(), v -> this.doClickOnFoodItem(v));

        this.allergyFilterButton.setOnClickListener(v -> navigateTo(R.id.action_searchResultFragment_to_allergyFragment));

        this.manufacturerChip.setOnCheckedChangeListener((buttonView, isChecked) -> this.doSort(isChecked, Constant.SortElement.MANUFACTURER));
        this.rankingChip.setOnCheckedChangeListener((buttonView, isChecked) -> this.doSort(isChecked, Constant.SortElement.RANK));
        this.reviewCountChip.setOnCheckedChangeListener((buttonView, isChecked) -> this.doSort(isChecked, Constant.SortElement.REVIEW_COUNT));
        this.rankingChip.performClick();

        this.adRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext(), LinearLayoutManager.HORIZONTAL, false));
        this.adRecyclerView.setAdapter(this.foodRecyclerAdapter);
        this.adRecyclerView.setNestedScrollingEnabled(false);

        this.resultRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        this.resultRecyclerView.setAdapter(this.foodRecyclerAdapter);
        this.resultRecyclerView.setNestedScrollingEnabled(false);

        this.nestedScrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (v.getChildAt(v.getChildCount() - 1) != null) {
                if ((scrollY >= (v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight())) &&
                        scrollY > oldScrollY) {
                    loadMore();
                }
            }
        });

        this.searchFieldEditText.setText(this.searchModel.getSearchText());
        this.searchFieldEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
//                this.loadSearchResult();
                this.searchModel.setSearchPageNum(1);
                this.isLoadingMore = false;
                this.searchModel.setSearchText(this.searchFieldEditText.getText().toString().replace(' ','_'));
                this.saveSearch();
            }
            return false;
        });

        this.deleteIcon.setOnClickListener(v -> this.searchFieldEditText.setText(""));

        this.orderButton.setOnClickListener(v-> this.setOrder());

    }



    @Override
    protected boolean isLoginNeeded() {
        return false;
    }

    @Override
    protected void katiEntityUpdatedAndLogin() {
        this.searchModel.setFiltered(true);
        this.saveSearch();

        this.allergyFilterButton.setEnabled(this.searchModel.isFiltered());
        this.allergyFilterButton.setVisibility(View.VISIBLE);

        this.getAllergyData(this.dataset.get(KatiEntity.EKatiData.AUTHORIZATION));

        this.loadAdvertisement();
    }

    @Override
    protected void katiEntityUpdatedAndNoLogin() {
        this.searchModel.setFiltered(false);
        this.saveSearch();

        this.allergyFilterButton.setVisibility(View.GONE);
        this.loadAdvertisement();

        this.refresh();

    }


    @Override
    public void onResume() {
        super.onResume();
        this.save();
        this.saveSearch();
        if (this.rankingChip != null)
            this.rankingChip.performClick();
    }

    @Override
    protected void searchModelDataUpdated() {
        if (this.dataset != null) {
            this.loadSearchResult();
            this.hideKeyboard();
        }
    }

    /**
     * callback
     */
    private class ReadUserAllergyShowCallBack extends SimpleLoginRetrofitCallBack<ReadUserAllergyResponse> {
        public ReadUserAllergyShowCallBack(Activity activity) {
            super(activity);
        }

        @Override
        public void onSuccessResponse(Response<ReadUserAllergyResponse> response) {
            Vector<String> vector = new Vector<>();
            vector.addAll(response.body().getUserAllergyMaterials());
            searchModel.setAllergyList(vector);
            refresh();
        }
    }

    private class AdListRequestCallback extends SimpleRetrofitCallBackImpl<List<AdvertisementResponse>> {
        public AdListRequestCallback(Activity activity) {
            super(activity);
        }

        @Override
        public void onSuccessResponse(Response<List<AdvertisementResponse>> response) {
            Vector<AdvertisementResponse> items = new Vector<>(response.body());
            adRecyclerAdapter.setItems(items);
            adRecyclerView.setAdapter(adRecyclerAdapter);
        }
    }

    private class SearchRequestCallback extends SimpleRetrofitCallBackImpl<FindFoodBySortingResponse> {
        public SearchRequestCallback(Activity activity) {
            super(activity);
        }

        @Override
        public void onSuccessResponse(Response<FindFoodBySortingResponse> response) {
            FindFoodBySortingResponse dto = response.body();

            hasNext = dto.isHas_next();
            searchModel.setSearchPageNum(dto.getCurrent_page() + 1);

            if (!isLoadingMore) vector.clear();
            vector.addAll(response.body().getData());
            foodRecyclerAdapter.setItems(vector);
            foodRecyclerAdapter.notifyDataSetChanged();
            isLoadingMore = false;
        }
    }


    private void loadAdvertisement() {
        KatiRetrofitTool.getAPI().getAdFoodList(3).enqueue(JSHRetrofitTool.getCallback(new AdListRequestCallback(getActivity())));
    }

    private void loadMore() {
        if (hasNext) {
            this.searchModel.setSearchPageNum(this.searchModel.getSearchPageNum() + 1);
            this.isLoadingMore = true;
            this.saveSearch();
        }
    }
    private void refresh() {
            this.searchModel.setFoodSortElement(Constant.SortElement.RANK.getMessage());
            this.rankingChip.setChecked(true);
            this.searchModel.setSearchPageNum(1);
            this.isLoadingMore = false;
            this.saveSearch();
    }

    private void loadSearchResult() {

        Vector<Constant.ECompany> vector= new Vector();
        vector.addAll(Arrays.asList(Constant.ECompany.values()));

        boolean found=false;

        for(Constant.ECompany company: vector){
            if(this.searchModel.getSearchText().equals(company.name())) {
                found=true;
            break;}
        }

        if (!found) {
            KatiRetrofitTool.getAPI().getNameFoodListBySorting(
                    this.searchModel.getSearchPageNum(),
                    this.searchModel.getPageSize(),
                    this.searchModel.getFoodSortElement(),
                    this.searchModel.getSortOrder().getMessage(),
                    this.searchModel.getSearchText(),
                    !this.searchModel.isFiltered() ? null : this.searchModel.getAllergyList()
            ).enqueue(JSHRetrofitTool.getCallback(new SearchRequestCallback(this.getActivity())));
        } else {
            KatiRetrofitTool.getAPI().getManufacturerFoodListBySorting(
                    this.searchModel.getSearchPageNum(),
                    this.searchModel.getPageSize(),
                    this.searchModel.getFoodSortElement(),
                    this.searchModel.getSortOrder().getMessage(),
                    this.searchModel.getSearchText(),
                    !this.searchModel.isFiltered() ? null : this.searchModel.getAllergyList()
            ).enqueue(JSHRetrofitTool.getCallback(new SearchRequestCallback(this.getActivity())));
        }
    }

    private void setOrder() {
        this.nestedScrollView.scrollTo(0,0);
        if(this.searchModel.getSortOrder().equals(Constant.SortOrder.asc)){
            this.searchModel.setSortOrder(Constant.SortOrder.desc);
            this.searchModel.setSearchPageNum(1);
            this.orderButton.setImageDrawable(getContext().getDrawable(R.drawable.ic_baseline_swap_vert_24_descend));
        }else{
            this.searchModel.setSortOrder(Constant.SortOrder.asc);
            this.searchModel.setSearchPageNum(1);
            this.orderButton.setImageDrawable(getContext().getDrawable(R.drawable.ic_baseline_swap_vert_24_assend));
        }
        this.saveSearch();
    }

    private void hideKeyboard(){
        ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(searchFieldEditText.getWindowToken(), 0);
    }



    private void doSort(boolean isChecked, Constant.SortElement element) {
        if (isChecked) {
            this.nestedScrollView.scrollTo(0,0);
            this.searchModel.setSearchPageNum(1);
            this.searchModel.setFoodSortElement(element.getMessage());
            this.saveSearch();
        }
    }


    private void getAllergyData(String token) {
        KatiRetrofitTool.getAPIWithAuthorizationToken(token).readUserAllergy().
                enqueue(JSHRetrofitTool.getCallback(new ReadUserAllergyShowCallBack(getActivity())));
    }

    private void doClickOnAdItem(View v) {
        Intent intent = new Intent(getActivity(), FoodDetailActivity.class);
        intent.putExtra(DETAIL_PRODUCT_INFO_TABLE_FRAGMENT_FOOD_ID_EXTRA, (Long) v.getTag());
        intent.putExtra(NEW_DETAIL_ACTIVITY_EXTRA_IS_AD, true);
        startActivity(intent);
    }

    private void doClickOnFoodItem(View v) {
        Intent intent = new Intent(getActivity(), FoodDetailActivity.class);
        intent.putExtra(DETAIL_PRODUCT_INFO_TABLE_FRAGMENT_FOOD_ID_EXTRA, (Long) v.getTag());
        intent.putExtra(NEW_DETAIL_ACTIVITY_EXTRA_IS_AD, false);
        startActivity(intent);
    }
}