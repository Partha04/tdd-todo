package com.tdd.todo.service;

import com.tdd.todo.dto.CreateTodoRequest;
import com.tdd.todo.dto.TodoResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface TodoService {
    TodoResponse addTodo(CreateTodoRequest createTodoRequest);

    List<TodoResponse> getAllTodo();

    TodoResponse getTodoByID(UUID id);
}
