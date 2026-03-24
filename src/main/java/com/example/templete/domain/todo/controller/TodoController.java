package com.example.templete.domain.todo.controller;

import com.example.templete.domain.todo.model.TodoResponse;
import com.example.templete.domain.todo.service.TodoService;
import com.example.templete.global.common.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/todo")
@Tag(name = "TODO 컨트롤러")
@RequiredArgsConstructor
public class   TodoController {
    private final TodoService todoService;

    @GetMapping("/{todoId}")
    @Operation(summary = "특정 id에 해당하는 todo를 조회합니다. ")
    public ApiResponse<TodoResponse> selectTodoById(@PathVariable int todoId) {
        return ApiResponse.success(todoService.selectTodoById(todoId));
    }
}
