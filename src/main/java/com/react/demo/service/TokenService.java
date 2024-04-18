package com.react.demo.service;

import com.react.demo.dto.CreateAccessTokenResponse;
import com.react.demo.entity.RefreshToken;
import com.react.demo.entity.User;
import com.react.demo.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;

@RequiredArgsConstructor
@Service
public class TokenService {

    private final TokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;

    public CreateAccessTokenResponse tokenRefresh(String refreshToken) throws Exception {
        if(!tokenProvider.validToken(refreshToken))
            throw new IllegalAccessException("Unexpected token");

        RefreshToken existRefreshToken = refreshTokenService
                .findByRefreshToken(refreshToken);

        User user = existRefreshToken.getUser();

        String accessToken = tokenProvider.createAccessToken(user, Duration.ofHours(2));
        String newRefreshToken = existRefreshToken
                .update(tokenProvider.createRefreshToken(Duration.ofDays(1)))
                .getRefreshToken();

        return new CreateAccessTokenResponse(accessToken, newRefreshToken);

    }
}
