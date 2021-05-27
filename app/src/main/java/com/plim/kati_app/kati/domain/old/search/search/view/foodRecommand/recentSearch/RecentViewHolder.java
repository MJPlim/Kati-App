package com.plim.kati_app.kati.domain.old.search.search.view.foodRecommand.recentSearch;

import android.view.View;
import android.widget.Button;

import androidx.recyclerview.widget.RecyclerView;

import com.plim.kati_app.R;

public class RecentViewHolder extends RecyclerView.ViewHolder {

    // Associate
        // View
        private Button valueButton;

    // Constructor
    public RecentViewHolder(View itemView, View.OnLongClickListener onLongClickListener) {
        super(itemView);
        this.valueButton = itemView.findViewById(R.id.item_button);
        this.valueButton.setOnLongClickListener(onLongClickListener);
    }

    public void setValueButton(String value, View.OnClickListener listener) {
        this.valueButton.setText(value);
        this.valueButton.setTag(value);
        this.valueButton.setOnClickListener(listener);
    }
}
