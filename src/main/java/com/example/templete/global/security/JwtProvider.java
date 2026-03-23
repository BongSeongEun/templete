package com.example.templete.global.security;

import com.example.templete.domain.user.model.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

@Component
public class JwtProvider {
    @Value("${em.jwt.secret}")
    private String secretKey;
    @Value("${em.jwt.expireTime}")
    private Long accessExpireTime;
    @Value("${em.jwt.refreshExpireTime}")
    private Long refreshExpireTime;

    private SecretKey key;

    @PostConstruct
    protected void init() {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    public TokenInfo createToken(String userUuid, String userName, Role role) {

        Date now = new Date();
        Date accessTokenValidity = new Date(now.getTime() + accessExpireTime);
        String accessToken = Jwts.builder()
                .subject(userUuid)
                .claim("userName", userName)
                .claim("role", role)
                .expiration(accessTokenValidity)
                .signWith(key)
                .compact();

        Date refreshTokenValidity = new Date(now.getTime() + refreshExpireTime);
        String refreshToken = Jwts.builder()
                .expiration(refreshTokenValidity)
                .signWith(key)
                .compact();

        return new TokenInfo(accessToken, refreshToken);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public UsernamePasswordAuthenticationToken getAuthentication(String token) {
        Claims claims = Jwts.parser().verifyWith(key).build()
                .parseSignedClaims(token).getPayload();

        String userUuid = claims.getSubject();
        String role = claims.get("role", String.class);
        List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(role));

        return new UsernamePasswordAuthenticationToken(userUuid, null, authorities);
    }
}
