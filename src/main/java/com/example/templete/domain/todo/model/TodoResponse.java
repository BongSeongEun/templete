package com.example.templete.domain.todo.model;

import java.sql.Timestamp;

public record TodoResponse (
    Long todoId,
    String todoName,
    Timestamp todoStartTime,
    Timestamp todoEndTime,
    Boolean todoStatus){
}
