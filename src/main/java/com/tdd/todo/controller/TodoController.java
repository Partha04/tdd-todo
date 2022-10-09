package com.tdd.todo.controller;

import com.tdd.todo.dto.TodoResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@RequestMapping("/todo")
public class TodoController {

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    TodoResponse createNewTodo(){
        return null;
    }
}
