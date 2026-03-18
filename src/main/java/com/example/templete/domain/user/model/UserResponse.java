package com.example.templete.domain.user.model;

import lombok.Getter;

import java.util.UUID;

@Getter
public class UserResponse {
    private UUID userUuid;
    private String userName;
}
