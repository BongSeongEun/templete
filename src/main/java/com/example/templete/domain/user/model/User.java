package com.example.templete.domain.user.model;

import lombok.Getter;

import java.util.UUID;

@Getter
public class User {
    private Long userId;
    private UUID userUuid;
    private String userName;
    private String loginId;
    private String loginPassword;
    private Role role;
}
