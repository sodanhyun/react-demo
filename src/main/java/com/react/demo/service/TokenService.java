package com.react.demo.service;

import com.react.demo.dto.CreateAccessTokenResponse;
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

    public CreateAccessTokenResponse tokenRefresh(String refreshToken) {
        if(!tokenProvider.validToken(refreshToken))
            throw new IllegalArgumentException("Unexpected token");

        User User = refreshTokenService.findByRefreshToken(refreshToken).getUser();

        return new CreateAccessTokenResponse(
                tokenProvider.createAccessToken(User, Duration.ofHours(2)),
                refreshToken);
    }
}
