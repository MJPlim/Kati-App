package com.plim.kati_app.kati.domain.login.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter
@Getter
public class SocialLoginRequest {
    private String loginType;
    private String accessToken;
}
