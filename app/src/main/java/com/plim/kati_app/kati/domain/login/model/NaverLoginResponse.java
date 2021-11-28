package com.plim.kati_app.kati.domain.login.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class NaverLoginResponse {
    private String id;
    private String nickname;
    private String email;
}
