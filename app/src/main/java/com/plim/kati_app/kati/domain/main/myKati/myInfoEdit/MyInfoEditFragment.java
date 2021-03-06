package com.plim.kati_app.kati.domain.main.myKati.myInfoEdit;

import android.app.Activity;
import android.content.Intent;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.plim.kati_app.R;
import com.plim.kati_app.jshCrossDomain.tech.retrofit.JSHRetrofitTool;
import com.plim.kati_app.kati.crossDomain.domain.model.KatiEntity;
import com.plim.kati_app.kati.crossDomain.domain.view.dialog.KatiDialog;
import com.plim.kati_app.kati.crossDomain.domain.view.etc.JSHInfoItem;
import com.plim.kati_app.kati.crossDomain.domain.view.fragment.KatiInfoEditFragment;
import com.plim.kati_app.kati.crossDomain.tech.retrofit.KatiRetrofitTool;
import com.plim.kati_app.kati.domain.editSingleData.EditAddressActivity;
import com.plim.kati_app.kati.domain.editSingleData.EditBirthActivity;
import com.plim.kati_app.kati.domain.editSingleData.EditNameActivity;
import com.plim.kati_app.kati.domain.editPassword.view.EditPasswordActivity;
import com.plim.kati_app.kati.domain.main.myKati.myInfoEdit.model.GetSecondEmailResponse;
import com.plim.kati_app.kati.domain.setRestoreEmail.SetRestoreEmailActivity;
import com.plim.kati_app.kati.domain.signOut.SignOutActivity;
import com.plim.kati_app.kati.domain.main.myKati.myInfoEdit.model.UserInfoResponse;

import java.util.Vector;

import retrofit2.Response;

import static com.plim.kati_app.kati.crossDomain.domain.model.Constant.EDIT_SINGLE_DATA_EXTRA_ADDRESS;
import static com.plim.kati_app.kati.crossDomain.domain.model.Constant.EDIT_SINGLE_DATA_EXTRA_BIRTH;
import static com.plim.kati_app.kati.crossDomain.domain.model.Constant.EDIT_SINGLE_DATA_EXTRA_NAME;
import static com.plim.kati_app.kati.crossDomain.domain.model.Constant.LOG_OUT_ACTIVITY_FAILURE_DIALOG_MESSAGE;
import static com.plim.kati_app.kati.crossDomain.domain.model.Constant.LOG_OUT_ACTIVITY_FAILURE_DIALOG_TITLE;
import static com.plim.kati_app.kati.crossDomain.domain.model.Constant.LOG_OUT_ACTIVITY_SUCCESSFUL_DIALOG_MESSAGE;
import static com.plim.kati_app.kati.crossDomain.domain.model.Constant.LOG_OUT_ACTIVITY_SUCCESSFUL_DIALOG_TITLE;
import static com.plim.kati_app.kati.crossDomain.domain.model.Constant.NO_ADDRESS_DATA;
import static com.plim.kati_app.kati.crossDomain.domain.model.Constant.NO_BIRTH_DATA;
import static com.plim.kati_app.kati.crossDomain.domain.model.Constant.NO_RESTORE_DATA;

public class MyInfoEditFragment extends KatiInfoEditFragment {

    //component
    //view
    private Button editRestoreEmailButton, editPasswordButton, editNameButton, changeAddressButton, changeBirthButton;
    private JSHInfoItem restoreEmailItem, editPasswordItem, changeNameItem, changeAddressItem, changeBirthItem;
    private TextView logOut, signOut;

    private Vector<KatiDialog> dialogs;
    private String secondEmail = null;
    private boolean loginNeed = true;

    public MyInfoEditFragment() {
        this.dialogs = new Vector<>();
    }

    @Override
    public void onDestroy() {
        for (KatiDialog dialog : dialogs) {
            dialog.dismiss();
            dialogs.remove(dialog);
        }
        super.onDestroy();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_my_info_edit;
    }

    @Override
    protected void associateView(View view) {
        super.associateView(view);
        this.editPasswordButton = view.findViewById(R.id.myInfoEditFragment_editPasswordButton);
        this.editNameButton = view.findViewById(R.id.myInfoEditFragment_editNameButton);
        this.editRestoreEmailButton = view.findViewById(R.id.myInfoEditFragment_restoreEmailButton);
        this.changeAddressButton = view.findViewById(R.id.myInfoEditFragment_changeAddressButton);
        this.changeBirthButton = view.findViewById(R.id.myInfoEditFragment_changeBirthButton);

        this.restoreEmailItem = view.findViewById(R.id.myInfoEditFragment_restoreEmailItem);
        this.editPasswordItem = view.findViewById(R.id.myInfoEditFragment_editPasswordItem);
        this.changeNameItem = view.findViewById(R.id.myInfoEditFragment_changeNameItem);
        this.changeAddressItem = view.findViewById(R.id.myInfoEditFragment_changeAddressItem);
        this.changeBirthItem = view.findViewById(R.id.myInfoEditFragment_changeBirthItem);

        this.logOut = view.findViewById(R.id.myInfoEditFragment_logOutTextView);
        this.signOut = view.findViewById(R.id.myInfoEditFragment_signOutTextView);
    }

    @Override
    protected void initializeView() {
        super.initializeView();
        this.editPasswordButton.setOnClickListener(v -> this.getActivity().startActivity(new Intent(this.getContext(), EditPasswordActivity.class)));
        this.editRestoreEmailButton.setOnClickListener(v -> this.getActivity().startActivity(new Intent(this.getContext(), SetRestoreEmailActivity.class)));


        this.editNameButton.setOnClickListener(v -> this.moveActivity(EditNameActivity.class));
        this.changeAddressButton.setOnClickListener(v -> this.moveActivity(EditAddressActivity.class));
        this.changeBirthButton.setOnClickListener(v -> this.moveActivity(EditBirthActivity.class));


        this.logOut.setOnClickListener(v -> this.logOut());
        this.signOut.setOnClickListener(v -> this.getActivity().startActivity(new Intent(this.getContext(), SignOutActivity.class)));
    }


    @Override
    protected boolean isLoginNeeded() {
        return this.loginNeed;
    }

    @Override
    protected void katiEntityUpdatedAndLogin() {
        this.getUserData(this.dataset.get(KatiEntity.EKatiData.AUTHORIZATION));
        this.getSecondEmail(this.dataset.get(KatiEntity.EKatiData.AUTHORIZATION));
    }


    @Override
    public void infoModelDataUpdated() {
        this.changeNameItem.setContentText(this.userInfoResponse.getName());
        this.changeBirthItem.setContentText(this.userInfoResponse.getBirth() == null ? NO_BIRTH_DATA : this.userInfoResponse.getBirth());
        this.changeAddressItem.setContentText(this.userInfoResponse.getAddress() == null || this.userInfoResponse.getAddress().equals(KatiEntity.EKatiData.NULL.name()) ? NO_ADDRESS_DATA : this.userInfoResponse.getAddress());

        this.restoreEmailItem.setContentText(this.secondEmail == null ? NO_RESTORE_DATA : this.secondEmail);

    }


    private class ReadUserDataCallBack extends SimpleLoginRetrofitCallBack<UserInfoResponse> {
        public ReadUserDataCallBack(Activity activity) {
            super(activity);
        }

        @Override
        public void onSuccessResponse(Response<UserInfoResponse> response) {
            userInfoResponse = response.body();
            saveInfo();
        }
    }

    private class ReadSecondEmailCallback extends SimpleLoginRetrofitCallBack<GetSecondEmailResponse> {
        public ReadSecondEmailCallback(Activity activity) {
            super(activity);
        }

        @Override
        public void onSuccessResponse(Response<GetSecondEmailResponse> response) {
            secondEmail = response.body().getSecondEmail();
            saveInfo();
        }
    }

    /**
     * method
     */

    private void getUserData(String header) {
        KatiRetrofitTool.getAPIWithAuthorizationToken(header).getUserInfo().enqueue(JSHRetrofitTool.getCallback(new ReadUserDataCallBack(getActivity())));
    }

    private void getSecondEmail(String token) {
        KatiRetrofitTool.getAPIWithAuthorizationToken(token).getSecondEmail().enqueue(JSHRetrofitTool.getCallback(new ReadSecondEmailCallback(getActivity())));
    }

    private void moveActivity(Class activity) {
        Intent intent = new Intent(this.getActivity(), activity);
        intent.putExtra(EDIT_SINGLE_DATA_EXTRA_NAME, this.userInfoResponse.getName());
        intent.putExtra(EDIT_SINGLE_DATA_EXTRA_ADDRESS, this.userInfoResponse.getAddress());
        intent.putExtra(EDIT_SINGLE_DATA_EXTRA_BIRTH, this.userInfoResponse.getBirth());
        this.startActivity(intent);
    }

    private void logOut() {
        if (!this.dataset.get(KatiEntity.EKatiData.AUTHORIZATION).equals(KatiEntity.EKatiData.NULL.name())) {
            this.showOkDialog();
        } else {
            this.showNoDialog();
        }
    }

    public void showOkDialog() {
        this.dialogs.add(KatiDialog.simplerTwoOptionAlertDialog(
                getActivity(),
                LOG_OUT_ACTIVITY_SUCCESSFUL_DIALOG_TITLE,
                LOG_OUT_ACTIVITY_SUCCESSFUL_DIALOG_MESSAGE,
                (dialog, which) -> {
                    removeLoginData();
                    navigateTo(R.id.action_myInfoEditFragment_to_homeFragment);
                },
                null
                )
        );
    }

    private void removeLoginData() {
        this.loginNeed = false;
        this.dataset.put(KatiEntity.EKatiData.EMAIL, KatiEntity.EKatiData.NULL.name());
        this.dataset.put(KatiEntity.EKatiData.PASSWORD, KatiEntity.EKatiData.NULL.name());
        this.dataset.put(KatiEntity.EKatiData.NAME, KatiEntity.EKatiData.NULL.name());
        this.dataset.put(KatiEntity.EKatiData.AUTO_LOGIN, KatiEntity.EKatiData.FALSE.name());
        this.dataset.put(KatiEntity.EKatiData.AUTHORIZATION, KatiEntity.EKatiData.NULL.name());
        this.save();
    }

    public void showNoDialog() {
        this.dialogs.add(this.showDialog(LOG_OUT_ACTIVITY_FAILURE_DIALOG_TITLE, LOG_OUT_ACTIVITY_FAILURE_DIALOG_MESSAGE, null));
    }


}