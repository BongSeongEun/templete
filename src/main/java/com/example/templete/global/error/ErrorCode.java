package com.example.templete.global.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    TODO_NOT_FOUND("TODO-0001", "해당하는 id의 todo가 존재하지 않습니다.", HttpStatus.NOT_FOUND),
    USER_NOT_FOUND("USER-0001", "해당하는 id의 user가 존재하지 않습니다.", HttpStatus.NOT_FOUND),
    INVALID_PASSWORD("USER-0002", "비밀번호가 일치하지 않습니다.", HttpStatus.BAD_REQUEST),;

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;

    ErrorCode(String code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
