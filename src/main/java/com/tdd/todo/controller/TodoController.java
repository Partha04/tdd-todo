package com.tdd.todo.controller;

import com.tdd.todo.dto.TodoCreateRequest;
import com.tdd.todo.dto.TodoResponse;
import com.tdd.todo.dto.TodoUpdateRequest;
import com.tdd.todo.service.TodoService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/todo")
public class TodoController {
    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    TodoResponse createNewTodo(@RequestBody TodoCreateRequest createTodoDto) {
        return todoService.addTodo(createTodoDto);
    }

    @GetMapping
    @ResponseStatus(value = HttpStatus.OK)
    List<TodoResponse> getAllTodos() {
        return todoService.getAllTodo();
    }

    @GetMapping("/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    TodoResponse getTodoById(@PathVariable UUID id) {
        return todoService.getTodoByID(id);
    }


    @PutMapping("/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    TodoResponse updateTodoById(@PathVariable UUID id, @RequestBody TodoUpdateRequest todoUpdateRequest) {
        return todoService.updateTodo(id, todoUpdateRequest);
    }

    @DeleteMapping(("/{id}"))
    @ResponseStatus(value = HttpStatus.OK)
    TodoResponse deleteTodoById(@PathVariable UUID id) {
        return null;
    }

}
