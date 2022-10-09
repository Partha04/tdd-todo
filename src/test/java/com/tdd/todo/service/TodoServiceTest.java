package com.tdd.todo.service;

import com.tdd.todo.dto.CreateTodoRequest;
import com.tdd.todo.dto.TodoResponse;
import com.tdd.todo.model.Todo;
import com.tdd.todo.repository.TodoRepository;
import com.tdd.todo.testContainers.PostgresTestContainer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class TodoServiceTest extends PostgresTestContainer {
    @Autowired
    TodoService todoService;
    @Autowired
    TodoRepository todoRepository;

    @Test
    void addTodoGivesNewlyCreatedTodoWithId() {
        CreateTodoRequest todoRequest = new CreateTodoRequest("new Todo");
        TodoResponse todoResponse = todoService.addTodo(todoRequest);
        assertNotNull(todoResponse.getId());
        assertEquals(todoRequest.getTask(), todoResponse.getTask());
        assertFalse(todoResponse.isCompleted());
    }

    @Test
    void addTodoShouldSaveInDatabase() {
        CreateTodoRequest todoRequest = new CreateTodoRequest("new Todo");
        TodoResponse todoResponse = todoService.addTodo(todoRequest);
        Optional<Todo> todoOptional = todoRepository.findById(todoResponse.getId());
        assertTrue(todoOptional.isPresent());
    }

}