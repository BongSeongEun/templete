package com.example.templete.domain.user.controller;

import com.example.templete.domain.user.model.UserLoginRequest;
import com.example.templete.domain.user.model.UserSignUpRequest;
import com.example.templete.domain.user.service.LoginService;
import com.example.templete.global.common.ApiResponse;
import com.example.templete.global.security.TokenInfo;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class LoginController {
    private final LoginService loginService;

    @Autowired
    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping("/signUp")
    public ApiResponse<Void> signUp(@RequestBody UserSignUpRequest userSignUpRequest){
        loginService.signUp(userSignUpRequest);
        return ApiResponse.success();
    }

    @PostMapping("/login")
    public ApiResponse<Void> login(@RequestBody UserLoginRequest userLoginRequest, HttpServletResponse response) {
        TokenInfo tokenInfo = loginService.login(userLoginRequest);
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
        return ApiResponse.success();
    }

}
