package com.tdd.todo.service.impl;

import com.tdd.todo.dto.CreateTodoRequest;
import com.tdd.todo.dto.TodoResponse;
import com.tdd.todo.model.Todo;
import com.tdd.todo.repository.TodoRepository;
import com.tdd.todo.service.TodoService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TodoServiceImpl implements TodoService {
    @Autowired
    TodoRepository todoRepository;

    ModelMapper mapper = new ModelMapper();

    public TodoResponse addTodo(CreateTodoRequest createTodoRequest) {
        Todo todo = mapper.map(createTodoRequest, Todo.class);
        Todo savedTodo = todoRepository.save(todo);
        return mapper.map(savedTodo, TodoResponse.class);
    }

    @Override
    public List<TodoResponse> getAllTodo() {
        return null;
    }
}
