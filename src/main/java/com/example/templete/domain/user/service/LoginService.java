package com.example.templete.domain.user.service;

import com.example.templete.domain.user.mapper.UserMapper;
import com.example.templete.domain.user.model.User;
import com.example.templete.domain.user.model.UserLoginRequest;
import com.example.templete.domain.user.model.UserLoginResponse;
import com.example.templete.domain.user.model.UserSignUpRequest;
import com.example.templete.global.common.JwtProvider;
import com.example.templete.global.error.BaseException;
import com.example.templete.global.error.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class LoginService {
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    @Autowired
    public LoginService(UserMapper userMapper,  PasswordEncoder passwordEncoder, JwtProvider jwtProvider) {
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
    }

    public void signUp(UserSignUpRequest userSignUpRequest) {
        User user = new User(
                userSignUpRequest.userName(),
                userSignUpRequest.loginId(),
                passwordEncoder.encode(userSignUpRequest.loginPassword())
        );
        userMapper.insertUser(user);
    }

    public String login(UserLoginRequest userLoginRequest) {
        UserLoginResponse userLoginResponse = userMapper.selectUserById(userLoginRequest.loginId());
        if (userLoginResponse == null) {
            throw new BaseException(ErrorCode.USER_NOT_FOUND);
        }
        if(!passwordEncoder.matches(userLoginRequest.loginPassword(), userLoginResponse.loginPassword())){
            throw new BaseException(ErrorCode.INVALID_PASSWORD);
        }
        System.out.println("입력된 비번: " + userLoginRequest.loginPassword());
        System.out.println("DB에서 가져온 비번: " + userLoginResponse.loginPassword());
        return jwtProvider.createToken(userLoginResponse.userUuid(), userLoginResponse.userName(), userLoginResponse.role());
    }

}
