package com.plim.kati_app.kati.domain.main.search.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class FindFoodByBarcodeRequest {
    private String barcode;
}
