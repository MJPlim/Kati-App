package com.plim.kati_app.kati.domain.temp.itemRank.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemRankingResponse {
    private Long foodId;
    private String foodName;
    private String avgRating;
}
