package com.example.templete.domain.user.model;

public record UserLoginRequest(
    String loginId,
    String loginPassword) {
}
