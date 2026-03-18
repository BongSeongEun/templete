package com.example.templete.domain.user.model;

import java.util.UUID;

public record UserLoginResponse(
    String userUuid,
    String loginPassword,
    String userName,
    Role role) {
}
