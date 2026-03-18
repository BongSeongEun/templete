package com.example.templete.domain.user.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class User {
    private Long userId;
    private String userUuid;
    private String userName;
    private String loginId;
    private String loginPassword;
    private Role role;

    public User(String userName, String loginId, String loginPassword) {
        this.userName = userName;
        this.loginId = loginId;
        this.loginPassword = loginPassword;
    }
}
