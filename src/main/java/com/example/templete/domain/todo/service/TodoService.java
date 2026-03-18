package com.example.templete.domain.todo.service;

import com.example.templete.domain.todo.mapper.TodoMapper;
import com.example.templete.domain.todo.model.TodoResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TodoService {
    private final TodoMapper todoMapper;

    @Autowired
    public TodoService(TodoMapper todoMapper) {
        this.todoMapper = todoMapper;
    }

    public TodoResponse selectTodoById(int todoId) {
        return todoMapper.selectTodoById(todoId);
    }

}
