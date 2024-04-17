package com.react.demo.service;

import com.react.demo.dto.CreateAccessTokenResponse;
import com.react.demo.entity.Member;
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

        Member member = refreshTokenService.findByRefreshToken(refreshToken).getMember();

        return new CreateAccessTokenResponse(
                tokenProvider.createAccessToken(member, Duration.ofHours(2)),
                refreshToken);
    }
}
