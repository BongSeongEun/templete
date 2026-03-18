package com.example.templete.domain.todo.controller;

import com.example.templete.domain.todo.model.TodoResponse;
import com.example.templete.domain.todo.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/todo")
public class TodoController {
    private final TodoService todoService;

    @Autowired
    public TodoController (TodoService todoService) {
        this.todoService = todoService;
    }

    @GetMapping("/{todoId}")
    public TodoResponse selectTodoById(@PathVariable int todoId) {
        return todoService.selectTodoById(todoId);
    }
}
