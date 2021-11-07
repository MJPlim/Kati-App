package com.plim.kati_app.kati.domain.signUp.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SocialLoginModel {
    private String email, password, name;
}
