package com.tdd.todo.service.impl;

import com.tdd.todo.dto.TodoCreateRequest;
import com.tdd.todo.dto.TodoResponse;
import com.tdd.todo.dto.TodoUpdateRequest;
import com.tdd.todo.exception.EntityNotFoundException;
import com.tdd.todo.model.Todo;
import com.tdd.todo.repository.TodoRepository;
import com.tdd.todo.service.TodoService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TodoServiceImpl implements TodoService {
    @Autowired
    TodoRepository todoRepository;

    ModelMapper mapper = new ModelMapper();

    public TodoResponse addTodo(TodoCreateRequest todoCreateRequest) {
        Todo todo = mapper.map(todoCreateRequest, Todo.class);
        Todo savedTodo = todoRepository.save(todo);
        return mapper.map(savedTodo, TodoResponse.class);
    }

    @Override
    public List<TodoResponse> getAllTodo() {
        List<Todo> todoList = todoRepository.findAll();
        ArrayList<TodoResponse> todoResponses = new ArrayList<>();
        for (Todo todo : todoList)
            todoResponses.add(mapper.map(todo, TodoResponse.class));
        return todoResponses;
    }

    @Override
    public TodoResponse getTodoByID(UUID id) {
        Optional<Todo> optionalTodo = todoRepository.findById(id);
        if (optionalTodo.isEmpty())
            throw new EntityNotFoundException("Todo not found");
        return mapper.map(optionalTodo.get(), TodoResponse.class);
    }

    @Override
    public TodoResponse updateTodo(UUID id, TodoUpdateRequest todoUpdateRequest) {
        Optional<Todo> optionalTodo = todoRepository.findById(id);
        if (optionalTodo.isEmpty())
            throw new EntityNotFoundException("Todo not found");
        Todo todo = optionalTodo.get();
        mapper.map(todoUpdateRequest, todo);
        Todo updatedTodo = todoRepository.save(todo);
        return mapper.map(updatedTodo, TodoResponse.class);
    }

    @Override
    public TodoResponse deleteById(UUID id) {
        Optional<Todo> optionalTodo = todoRepository.findById(id);
        if (optionalTodo.isEmpty())
            throw new EntityNotFoundException("Todo not found");
        todoRepository.deleteById(id);
        return mapper.map(optionalTodo.get(), TodoResponse.class);
    }

}
