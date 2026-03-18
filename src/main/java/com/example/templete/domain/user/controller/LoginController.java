package com.example.templete.domain.user.controller;

import com.example.templete.domain.user.mapper.UserMapper;
import com.example.templete.domain.user.model.UserLoginRequest;
import com.example.templete.domain.user.model.UserSignUpRequest;
import com.example.templete.domain.user.service.LoginService;
import com.example.templete.global.common.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping("/login")
    public ApiResponse<String> login(@RequestBody UserLoginRequest userLoginRequest) {
        String token = loginService.login(userLoginRequest);
        return ApiResponse.success(token);
    }

}
