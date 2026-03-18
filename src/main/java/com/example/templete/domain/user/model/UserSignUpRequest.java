package com.example.templete.domain.user.model;

public record UserSignUpRequest(
    String userName,
    String loginId,
    String loginPassword) {
}
