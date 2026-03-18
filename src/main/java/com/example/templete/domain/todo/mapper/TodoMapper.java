package com.example.templete.domain.todo.mapper;

import com.example.templete.domain.todo.model.TodoResponse;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TodoMapper {
    TodoResponse selectTodoById(int todoId);
}
