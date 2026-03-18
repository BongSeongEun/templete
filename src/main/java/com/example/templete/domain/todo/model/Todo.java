package com.example.templete.domain.todo.model;

import lombok.Getter;

import java.sql.Timestamp;

@Getter
public class Todo {
    private Long todoId;
    private String todoName;
    private Timestamp todoStartTime;
    private Timestamp todoEndTime;
    private Boolean todoStatus;
}
