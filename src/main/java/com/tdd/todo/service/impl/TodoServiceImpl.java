package com.tdd.todo.service.impl;

import com.tdd.todo.dto.CreateTodoRequest;
import com.tdd.todo.dto.TodoResponse;
import com.tdd.todo.repository.TodoRepository;
import com.tdd.todo.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TodoServiceImpl implements TodoService {
    @Autowired
    TodoRepository todoRepository;

    public TodoResponse addTodo(CreateTodoRequest createTodoRequest) {
        return new TodoResponse(UUID.randomUUID(), createTodoRequest.getTask(), false);
    }
}
