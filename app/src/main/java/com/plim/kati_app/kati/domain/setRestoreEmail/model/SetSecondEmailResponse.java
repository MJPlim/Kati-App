package com.plim.kati_app.kati.domain.setRestoreEmail.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SetSecondEmailResponse {
    private final String secondEmail;
}
