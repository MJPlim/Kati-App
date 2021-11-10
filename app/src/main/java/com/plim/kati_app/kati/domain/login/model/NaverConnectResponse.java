package com.plim.kati_app.kati.domain.login.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class NaverConnectResponse {
    private String resultcode;
    private NaverLoginResponse response;
}
