package com.react.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CreateAccessTokenResponse {
    private String accessToken;
    private String refreshToken;
    private String authority;

    @Builder
    public CreateAccessTokenResponse(String accessToken, String refreshToken, String authority) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.authority = authority;
    }
}
