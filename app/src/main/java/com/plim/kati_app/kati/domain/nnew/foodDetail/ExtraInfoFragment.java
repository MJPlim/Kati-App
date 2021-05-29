package com.plim.kati_app.kati.domain.nnew.foodDetail;

import android.content.Intent;
import android.net.Uri;

import android.view.View;
import android.widget.Button;

import com.plim.kati_app.R;
import com.plim.kati_app.kati.crossDomain.domain.view.etc.JSHInfoItem;
import com.plim.kati_app.kati.crossDomain.domain.view.fragment.KatiFoodFragment;

import static com.plim.kati_app.kati.crossDomain.domain.model.Constant.DETAIL_PRODUCT_INFO_FRAGMENT_ISSUE_LINK;
import static com.plim.kati_app.kati.crossDomain.domain.model.Constant.DETAIL_PRODUCT_INFO_FRAGMENT_SHOPPING_LINK_;

public class ExtraInfoFragment extends KatiFoodFragment {

    //associate
    //view
    private Button companyShowIssueButton, showOnlineBuyButton;
    private JSHInfoItem companyName;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_extra_info;
    }

    @Override
    protected void associateView(View view) {
        this.companyShowIssueButton = view.findViewById(R.id.extraInfoFragment_companyShowIssueButton);
        this.showOnlineBuyButton = view.findViewById(R.id.extraInfoFragment_showOnlineBuyButton);
        this.companyName = view.findViewById(R.id.extraInfoFragment_companyName);
    }

    @Override
    protected void initializeView() {
        this.companyName.setContentText(this.foodDetailResponse.getManufacturerName().split("_")[0]);

        this.companyShowIssueButton.setOnClickListener(v ->
                startActivity(
                        new Intent(Intent.ACTION_VIEW, Uri.parse(DETAIL_PRODUCT_INFO_FRAGMENT_ISSUE_LINK
                                + this.foodModel.getFoodDetailResponse().getValue().getManufacturerName().split("_")[0])
                        )));
        this.showOnlineBuyButton.setOnClickListener(v ->
                startActivity(
                        new Intent(Intent.ACTION_VIEW, Uri.parse(DETAIL_PRODUCT_INFO_FRAGMENT_SHOPPING_LINK_
                                + this.foodModel.getFoodDetailResponse().getValue().getFoodName())
                        ))
        );
    }

    @Override
    protected void katiEntityUpdated() {
    }

    @Override
    public void foodModelDataUpdated() {

    }

    @Override
    protected void summaryDataUpdated() {

    }


}