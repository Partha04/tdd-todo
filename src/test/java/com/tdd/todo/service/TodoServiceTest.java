package com.tdd.todo.service;

import com.tdd.todo.dto.CreateTodoRequest;
import com.tdd.todo.dto.TodoResponse;
import com.tdd.todo.exception.EntityNotFoundException;
import com.tdd.todo.model.Todo;
import com.tdd.todo.repository.TodoRepository;
import com.tdd.todo.testContainers.PostgresTestContainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class TodoServiceTest extends PostgresTestContainer {
    @Autowired
    TodoService todoService;
    @Autowired
    TodoRepository todoRepository;

    @Nested
    class AddTodosTests {

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

    @Nested
    class GetAllTodosTest {
        @BeforeEach
        void setUp() {
            todoRepository.deleteAll();
        }

        @Test
        void getSingleTodosFromDatabase() {
            Todo todo = new Todo(null, "task 1", false);
            todoRepository.save(todo);
            List<TodoResponse> allTodo = todoService.getAllTodo();
            assertEquals(1, allTodo.size());
            assertNotNull(allTodo.get(0).getId());
            assertEquals(todo.getTask(), allTodo.get(0).getTask());
            assertEquals(todo.isCompleted(), allTodo.get(0).isCompleted());
        }

        @Test
        void get10todosFromDatabase() {
            for (int i = 0; i < 10; i++)
                todoRepository.save(new Todo(UUID.randomUUID(), "task " + i, false));
            List<TodoResponse> allTodo = todoService.getAllTodo();
            assertEquals(10, allTodo.size());
        }

    }

    @Nested
    class GetATodoByID {
        @BeforeEach
        void setUp() {
            todoRepository.deleteAll();
        }

        @Test
        void shouldGiveExistingId() {
            Todo task1 = todoRepository.save(new Todo(null, "task1", true));
            TodoResponse todoByID = todoService.getTodoByID(task1.getId());

            assertEquals(task1.getId(), todoByID.getId());
            assertEquals(task1.getTask(), todoByID.getTask());
            assertEquals(task1.isCompleted(), todoByID.isCompleted());
        }

        @Test
        void shouldGiveErrorForNonExistingId() {
            EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> todoService.getTodoByID(UUID.randomUUID()));
            assertEquals("Todo not found", exception.getMessage());
        }
    }

}
