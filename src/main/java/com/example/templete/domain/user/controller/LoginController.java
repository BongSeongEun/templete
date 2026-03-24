package com.example.templete.domain.user.controller;

import com.example.templete.domain.user.model.UserLoginRequest;
import com.example.templete.domain.user.model.UserSignUpRequest;
import com.example.templete.domain.user.service.LoginService;
import com.example.templete.global.common.ApiResponse;
import com.example.templete.global.security.TokenInfo;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
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
    public ApiResponse<Void> signUp(@RequestBody UserSignUpRequest userSignUpRequest) {
        loginService.signUp(userSignUpRequest);
        return ApiResponse.success();
    }

    @PostMapping("/login")
    public ApiResponse<Void> login(@RequestBody UserLoginRequest userLoginRequest, HttpServletResponse response) {
        TokenInfo tokenInfo = loginService.login(userLoginRequest);
        loginService.setTokenCookies(response, tokenInfo);
        return ApiResponse.success();
    }

    @PostMapping("/renewToken")
    public ApiResponse<Void> renewToken(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = getRefreshToken(request);
        TokenInfo tokenInfo = loginService.renewToken(refreshToken);
        loginService.setTokenCookies(response, tokenInfo);
        return ApiResponse.success();
    }

    private String getRefreshToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refreshToken".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
