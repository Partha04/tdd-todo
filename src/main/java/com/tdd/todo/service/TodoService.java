package com.tdd.todo.service;

import com.tdd.todo.dto.CreateTodoRequest;
import com.tdd.todo.dto.TodoResponse;
import org.springframework.stereotype.Service;

@Service
public interface TodoService {
    TodoResponse addTodo(CreateTodoRequest createTodoRequest);
}
