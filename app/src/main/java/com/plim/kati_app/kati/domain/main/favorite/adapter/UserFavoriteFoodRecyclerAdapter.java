package com.plim.kati_app.kati.domain.main.favorite.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.plim.kati_app.R;
import com.plim.kati_app.kati.domain.foodDetail.FoodDetailActivity;
import com.plim.kati_app.kati.domain.main.favorite.model.UserFavoriteResponse;

import java.util.Vector;

import static com.plim.kati_app.kati.crossDomain.domain.model.Constant.NEW_DETAIL_ACTIVITY_EXTRA_FOOD_ID;
import static com.plim.kati_app.kati.crossDomain.domain.model.Constant.NEW_DETAIL_ACTIVITY_EXTRA_IS_AD;

public class UserFavoriteFoodRecyclerAdapter extends RecyclerView.Adapter<UserFavoriteFoodViewHolder> {

    // Associate
        // ETC
        private Activity activity;
        private Vector<UserFavoriteResponse> items;

    // Constructor
    public UserFavoriteFoodRecyclerAdapter(Activity activity) {
        this.items = new Vector<>();
        this.activity=activity;
    }

    @Override
    public UserFavoriteFoodViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food_old, parent, false);

        return new UserFavoriteFoodViewHolder(view, v->this.startNewDetailActivity((Long) v.getTag()), this.activity);
    }
    @Override
    public void onBindViewHolder(UserFavoriteFoodViewHolder holder, int position) {
        UserFavoriteResponse item = items.get(position);
        (holder).setValue(item);}
    @Override
    public int getItemCount() { return items.size(); }

    public void setItems(Vector<UserFavoriteResponse> items) { this.items=items; }
    private void startNewDetailActivity(Long foodId) {
        Intent intent = new Intent(this.activity, FoodDetailActivity.class);
        intent.putExtra(NEW_DETAIL_ACTIVITY_EXTRA_FOOD_ID, foodId);
        intent.putExtra(NEW_DETAIL_ACTIVITY_EXTRA_IS_AD, false);
        this.activity.startActivity(intent);
    }
}
