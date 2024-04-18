package com.react.demo.service;

import com.react.demo.dto.CreateAccessTokenResponse;
import com.react.demo.dto.LoginDto;
import com.react.demo.dto.UserFormDto;
import com.react.demo.entity.User;
import com.react.demo.entity.RefreshToken;
import com.react.demo.jwt.TokenProvider;
import com.react.demo.repository.UserRepository;
import com.react.demo.repository.RefreshTokenRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    public User findById(String memberId) {
        return userRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected user"));
    }

    public void signUp(UserFormDto dto) {
        User exist = userRepository.findById(dto.getId()).orElse(null);
        if(exist != null) {
            throw new RuntimeException("해당 아이디는 사용할 수 없습니다");
        }
        User user = User.createUser(dto, passwordEncoder);
        userRepository.save(user);
    }

    public CreateAccessTokenResponse signIn(LoginDto loginDto) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getId(), loginDto.getPassword());
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        User user = userRepository.findById(authentication.getName()).orElse(null);
        if(user == null) {
            throw new RuntimeException("존재하지 않는 유저입니다");
        }
        RefreshToken existRefreshToken = refreshTokenRepository.findByUser(user).orElse(null);
        String refreshToken = tokenProvider.createRefreshToken(Duration.ofDays(14));
        if(existRefreshToken == null) {
            refreshTokenRepository.save(new RefreshToken(user, refreshToken));
        }else {
            existRefreshToken.update(refreshToken);
        }
        String accessToken = tokenProvider.createAccessToken(user, Duration.ofHours(2));

        return new CreateAccessTokenResponse(accessToken, refreshToken);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 유저입니다."));
    }
}
