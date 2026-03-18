package com.example.templete.domain.todo.model;

import lombok.Getter;

import java.sql.Timestamp;

@Getter
public class TodoResponse {
    private Long todoId;
    private String todoName;
    private Timestamp todoStartTime;
    private Timestamp todoEndTime;
    private Boolean todoStatus;
}
