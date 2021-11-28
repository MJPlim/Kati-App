package com.plim.kati_app.kati.domain.main.search.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class AdvertisementResponse {
    private final Long id;
    private final FoodResponse food;
    private final Long impression;
}