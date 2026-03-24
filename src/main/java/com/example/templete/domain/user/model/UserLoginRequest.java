package com.example.templete.domain.user.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "로그인 요청 DTO")
public record UserLoginRequest(
        @Schema(description = "사용자 아이디", example = "user123")
        String loginId,
        @Schema(description = "사용자 비밀번호", example = "fwf213f!")
        String loginPassword) {
}
