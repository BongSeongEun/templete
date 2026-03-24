package com.example.templete.domain.todo.model;

import io.swagger.v3.oas.annotations.media.Schema;

import java.sql.Timestamp;

@Schema(description = "TODO 응답 DTO")
public record TodoResponse (
        @Schema(description = "TODO 아이디", example = "1")
        Long todoId,
        @Schema(description = "TODO 이름", example = "공부하기")
        String todoName,
        @Schema(description = "TODO 시작 시간", example = "2026-03-24T02:56:43.492Z")
        Timestamp todoStartTime,
        @Schema(description = "TODO 종료 시간", example = "2026-03-24T02:56:43.492Z")
        Timestamp todoEndTime,
        @Schema(description = "TODO 완료 상태", example = "true")
        Boolean todoStatus){
}
