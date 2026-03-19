package com.example.templete.global.common;

import com.example.templete.domain.user.model.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtProvider {
    @Value("${em.jwt.secret}")
    private String secretKey;
    @Value("${em.jwt.expireTime}")
    private Long accessExpireTime;
    @Value("${em.jwt.refreshExpireTime}")
    private Long refreshExpireTime;

    private Key key;

    @PostConstruct
    protected void init() {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    public TokenInfo createToken(String userUuid, String userName, Role role) {

        Date now = new Date();
        Date accessTokenValidity = new Date(now.getTime() + accessExpireTime);
        String accessToken = Jwts.builder()
                .setSubject(userUuid)
                .claim("userName", userName)
                .claim("role", role)
                .setExpiration(accessTokenValidity)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        Date refreshTokenValidity = new Date(now.getTime() + refreshExpireTime);
        String refreshToken = Jwts.builder()
                .setExpiration(refreshTokenValidity)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return new TokenInfo(accessToken, refreshToken);
    }
}
