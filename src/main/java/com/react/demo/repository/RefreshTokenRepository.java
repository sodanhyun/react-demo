package com.react.demo.repository;

import com.react.demo.entity.User;
import com.react.demo.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByUser(User User);
    Optional<RefreshToken> findByRefreshToken(String refreshToken);
}

