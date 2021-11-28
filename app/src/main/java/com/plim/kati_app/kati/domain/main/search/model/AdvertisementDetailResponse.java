package com.plim.kati_app.kati.domain.main.search.model;

import com.plim.kati_app.kati.domain.foodDetail.model.FoodDetailResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class AdvertisementDetailResponse {
    private final Long id;
    private final FoodDetailResponse food;
    private final Long impression;
}