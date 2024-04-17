package com.react.demo.service;

import com.react.demo.constant.CustomUser;
import com.react.demo.dto.CreateAccessTokenRequest;
import com.react.demo.dto.CreateAccessTokenResponse;
import com.react.demo.dto.LoginDto;
import com.react.demo.dto.MemberFormDto;
import com.react.demo.entity.Member;
import com.react.demo.entity.RefreshToken;
import com.react.demo.jwt.TokenProvider;
import com.react.demo.repository.MemberRepository;
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
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    public Member findById(String memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected user"));
    }

    public void signUp(MemberFormDto dto) {
        Member exist = memberRepository.findById(dto.getId()).orElse(null);
        if(exist != null) {
            throw new RuntimeException("해당 아이디는 사용할 수 없습니다");
        }
        Member member = Member.createMember(dto, passwordEncoder);
        memberRepository.save(member);
    }

    public CreateAccessTokenResponse signIn(LoginDto loginDto) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getId(), loginDto.getPassword());
        // authenticate 메소드가 실행이 될 때 CustomUserDetailsService class의 loadUserByUsername 메소드가 실행
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        Member member = memberRepository.findById(authentication.getName()).orElse(null);
        if(member == null) {
            throw new RuntimeException("존재하지 않는 유저입니다");
        }
        RefreshToken existRefreshToken = refreshTokenRepository.findByMember(member).orElse(null);
        String refreshToken = tokenProvider.createRefreshToken(Duration.ofDays(14));
        if(existRefreshToken == null) {
            refreshTokenRepository.save(new RefreshToken(member, refreshToken));
        }else {
            existRefreshToken.update(refreshToken);
        }
        String accessToken = tokenProvider.createAccessToken(member, Duration.ofHours(2));

        return new CreateAccessTokenResponse(accessToken, refreshToken);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 유저입니다."));
        return new CustomUser(member);
    }
}
