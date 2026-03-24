package com.example.templete.domain.todo.service;

import com.example.templete.domain.todo.mapper.TodoMapper;
import com.example.templete.domain.todo.model.TodoResponse;
import com.example.templete.global.error.BaseException;
import com.example.templete.global.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TodoService {
    private final TodoMapper todoMapper;

    public TodoResponse selectTodoById(int todoId) {
        TodoResponse todoResponse = todoMapper.selectTodoById(todoId);
        if(todoResponse == null) {
            throw new BaseException(ErrorCode.TODO_NOT_FOUND);
        }
        return todoResponse;
    }

}
