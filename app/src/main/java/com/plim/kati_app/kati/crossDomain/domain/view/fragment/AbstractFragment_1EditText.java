package com.plim.kati_app.kati.crossDomain.domain.view.fragment;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.plim.kati_app.R;
import com.plim.kati_app.kati.crossDomain.domain.model.KatiEntity;

public abstract class AbstractFragment_1EditText extends KatiViewModelFragment {

    // Associate
        // View
        protected TextView mainTextView, subTextView;
        protected EditText editText;
        protected Button button;

    /**
     * System Callback
     */
    @Override protected int getLayoutId() { return R.layout.abstract_layout; }
    @Override
    protected void associateView(View view) {
        this.mainTextView = view.findViewById(R.id.abstractRegisterLayout_mainTextView);
        this.subTextView = view.findViewById(R.id.abstractRegisterLayout_subTextView);
        this.editText = view.findViewById(R.id.abstractRegisterLayout_editText);
        this.button = view.findViewById(R.id.abstractRegisterLayout_button);
    }
    @Override
    protected void initializeView() {
        this.button.setOnClickListener(v->this.buttonClicked());
    }
    @Override
    protected void katiEntityUpdated() { }

    /**
     * Button Callback
     */
    protected abstract void buttonClicked();
}
