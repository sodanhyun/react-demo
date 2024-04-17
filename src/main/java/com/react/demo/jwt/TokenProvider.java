package com.react.demo.jwt;

import com.react.demo.entity.Member;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class TokenProvider {
    private static final String AUTHORITIES_KEY = "auth";
    private final JwtProperties jwtProperties;

    public String createAccessToken(Member member, Duration expiredAt) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expiredAt.toMillis());
        String authorities = member.getRole().getKey();

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setIssuer(jwtProperties.getIssuer())
                .setIssuedAt(now)
                .setExpiration(expiry)
                .setSubject(member.getId())
                .claim(AUTHORITIES_KEY, authorities)
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecretKey())
                .compact();

    }

    public String createRefreshToken(Duration expiredAt) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expiredAt.toMillis());

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setIssuer(jwtProperties.getIssuer())
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecretKey())
                .compact();
    }

    public boolean validToken(String token) {
        try{
            Jwts.parser()
                    .setSigningKey(jwtProperties.getSecretKey())
                    .parseClaimsJws(token);
            return true;
        }catch(MalformedJwtException e) {
            log.info("잘못되거나 권한이 없는 JWT 서명입니다");
            return false;
        }catch(ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.");
            return false;
        }catch(UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.");
            return false;
        }catch(IllegalArgumentException e) {
            log.info("JWT claim이 비어있습니다.");
            return false;
        }catch(Exception e) {
            log.info("유효하지 않은 JWT 토큰입니다.");
            return false;
        }
    }

    public Authentication getAuthentication(String token) {
        Claims claims = getClaims(token);
        if (claims.get(AUTHORITIES_KEY) == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());
        return new UsernamePasswordAuthenticationToken(new User(claims.getSubject(), "", authorities), token, authorities);
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(jwtProperties.getSecretKey())
                .parseClaimsJws(token)
                .getBody();
    }

    public String getUserId(String token) {
        Claims claims = getClaims(token);
        return claims.getSubject();
    }

    public String getRole(String token) {
        Claims claims = getClaims(token);
        return claims.get(AUTHORITIES_KEY).toString();
    }

}