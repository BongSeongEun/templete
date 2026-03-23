package com.example.templete.global.security;

public record TokenInfo(
    String accessToken,
    String refreshToken
) {
}
