package com.example.templete.domain.user.controller;

import com.example.templete.domain.user.model.UserLoginRequest;
import com.example.templete.domain.user.model.UserSignUpRequest;
import com.example.templete.domain.user.service.LoginService;
import com.example.templete.global.common.ApiResponse;
import com.example.templete.global.security.TokenInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "유저 인증 컨트롤러")
public class LoginController {
    private final LoginService loginService;

    @Autowired
    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping("/signUp")
    @Operation(summary = "아이디와 비밀번호, 그리고 닉네임을 이용해 회원가입을 진행합니다. ")
    public ApiResponse<Void> signUp(@RequestBody UserSignUpRequest userSignUpRequest) {
        loginService.signUp(userSignUpRequest);
        return ApiResponse.success();
    }

    @PostMapping("/login")
    @Operation(summary = "아이디와 비밀번호를 이용해 로그인을 진행합니다. ")
    public ApiResponse<Void> login(@RequestBody UserLoginRequest userLoginRequest, HttpServletResponse response) {
        TokenInfo tokenInfo = loginService.login(userLoginRequest);
        loginService.setTokenCookies(response, tokenInfo);
        return ApiResponse.success();
    }

    @PostMapping("/renewToken")
    @Operation(summary = "리프레시토큰을 이용해 토큰재발급을 진행합니다. ")
    public ApiResponse<Void> renewToken(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = getRefreshToken(request);
        TokenInfo tokenInfo = loginService.renewToken("RT: " + refreshToken);
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
