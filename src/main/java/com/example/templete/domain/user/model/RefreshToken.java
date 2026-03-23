package com.example.templete.domain.user.model;

import lombok.Getter;

import java.util.Date;

@Getter
public class RefreshToken {
    private Long refreshTokenId;
    private Long userUuid;
    private String refreshToken;
    private Date expiresAt;
    private Date createdAt;
}
