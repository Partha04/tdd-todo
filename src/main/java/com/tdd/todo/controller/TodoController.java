package com.tdd.todo.controller;

import com.tdd.todo.dto.CreateTodoRequest;
import com.tdd.todo.dto.TodoResponse;
import com.tdd.todo.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/todo")
public class TodoController {
    @Autowired
    TodoService todoService;

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    TodoResponse createNewTodo(@RequestBody CreateTodoRequest createTodoDto) {
        return todoService.addTodo(createTodoDto);
    }
}
