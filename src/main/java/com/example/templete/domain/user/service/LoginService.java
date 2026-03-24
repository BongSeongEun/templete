package com.example.templete.domain.user.service;

import com.example.templete.domain.user.mapper.UserMapper;
import com.example.templete.domain.user.model.User;
import com.example.templete.domain.user.model.UserLoginRequest;
import com.example.templete.domain.user.model.UserLoginResponse;
import com.example.templete.domain.user.model.UserSignUpRequest;
import com.example.templete.global.security.JwtProvider;
import com.example.templete.global.security.TokenInfo;
import com.example.templete.global.error.BaseException;
import com.example.templete.global.error.ErrorCode;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class LoginService {
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final StringRedisTemplate redisTemplate;

    @Value("${em.jwt.refreshExpireTime}")
    private Long refreshExpireTime;

    @Autowired
    public LoginService(UserMapper userMapper, PasswordEncoder passwordEncoder,
                        JwtProvider jwtProvider, StringRedisTemplate redisTemplate) {
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
        this.redisTemplate = redisTemplate;
    }

    public void signUp(UserSignUpRequest userSignUpRequest) {
        User user = new User(
                userSignUpRequest.userName(),
                userSignUpRequest.loginId(),
                passwordEncoder.encode(userSignUpRequest.loginPassword())
        );
        userMapper.insertUser(user);
    }

    public TokenInfo login(UserLoginRequest userLoginRequest) {
        UserLoginResponse userLoginResponse = userMapper.selectUserById(userLoginRequest.loginId());
        if (userLoginResponse == null) {
            throw new BaseException(ErrorCode.USER_NOT_FOUND);
        }
        if (!passwordEncoder.matches(userLoginRequest.loginPassword(), userLoginResponse.loginPassword())) {
            throw new BaseException(ErrorCode.INVALID_PASSWORD);
        }

        TokenInfo tokenInfo = jwtProvider.createToken(
                userLoginResponse.userUuid(), userLoginResponse.userName(), userLoginResponse.role());

        saveRefreshToken(tokenInfo.refreshToken(), userLoginResponse.userUuid());
        return tokenInfo;
    }

    public TokenInfo renewToken(String refreshToken) {
        String userUuid = redisTemplate.opsForValue().get(refreshToken);
        if (userUuid == null) {
            throw new BaseException(ErrorCode.INVALID_REFRESH_TOKEN);
        }
        redisTemplate.delete(refreshToken);

        UserLoginResponse user = userMapper.selectUserByUuid(userUuid);
        if (user == null) {
            throw new BaseException(ErrorCode.USER_NOT_FOUND);
        }

        TokenInfo tokenInfo = jwtProvider.createToken(user.userUuid(), user.userName(), user.role());
        saveRefreshToken(tokenInfo.refreshToken(), user.userUuid());
        return tokenInfo;
    }

    private void saveRefreshToken(String refreshToken, String userUuid) {
        redisTemplate.opsForValue().set(
                refreshToken, userUuid, refreshExpireTime, TimeUnit.MILLISECONDS);
    }

    public void setTokenCookies(HttpServletResponse response, TokenInfo tokenInfo) {
        ResponseCookie accessTokenCookie = ResponseCookie.from("accessToken", tokenInfo.accessToken())
                .httpOnly(true)
                .path("/")
                .maxAge(600)
                .sameSite("Strict") // 도메인 제한
                .build();

        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", tokenInfo.refreshToken())
                .httpOnly(true)
                .path("/")
                .maxAge(604800)
                .sameSite("Strict") // 도메인 제한
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, accessTokenCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());
    }
}
