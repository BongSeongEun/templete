package com.example.templete.domain.user.model;

public record UserLoginResponse(
    String userUuid,
    String loginPassword,
    String userName,
    Role role) {
}
