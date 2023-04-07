package com.tdd.todo.integrationTests;

import com.tdd.todo.dto.TodoCreateRequest;
import com.tdd.todo.dto.TodoUpdateRequest;
import com.tdd.todo.model.Todo;
import com.tdd.todo.repository.TodoRepository;
import com.tdd.todo.testContainers.PostgresTestContainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TodoIntegrationTest extends PostgresTestContainer {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    TodoRepository todoRepository;
    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        todoRepository.deleteAll();
    }

    @Test
    void shouldSaveNewTodo() throws Exception {
        //arrange
        TodoCreateRequest todoCreateRequest = new TodoCreateRequest("New task1");
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/todo");
        requestBuilder.contentType(MediaType.APPLICATION_JSON);
        requestBuilder.content(objectMapper.writeValueAsString(todoCreateRequest));
        //act
        ResultActions resultActions = mockMvc.perform(requestBuilder);
        //assert
        resultActions.andExpect(status().isCreated())
                .andExpect(jsonPath("id").isNotEmpty())
                .andExpect(jsonPath("task").value(todoCreateRequest.getTask()))
                .andExpect(jsonPath("completed").value(false));
        List<Todo> todoList = todoRepository.findAll();
        assertEquals(todoCreateRequest.getTask(), todoList.get(0).getTask());

    }

    @Test
    void shouldGetAllTodos() throws Exception {
        //arrange
        todoRepository.save(new Todo(null, "task0", false));
        todoRepository.save(new Todo(null, "task1", true));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/todo");
        //act
        ResultActions resultActions = mockMvc.perform(requestBuilder);
        //assert
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").isNotEmpty())
                .andExpect(jsonPath("$[0].task").value("task0"))
                .andExpect(jsonPath("$[0].completed").value(false))
                .andExpect(jsonPath("$[1].id").isNotEmpty())
                .andExpect(jsonPath("$[1].task").value("task1"))
                .andExpect(jsonPath("$[1].completed").value(true));
    }

    @Test
    void shouldGetTodoById() throws Exception {
        //arrange
        Todo task0 = todoRepository.save(new Todo(null, "task0", false));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/todo/{id}", task0.getId());
        //act
        ResultActions resultActions = mockMvc.perform(requestBuilder);
        //assert
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("id").isNotEmpty())
                .andExpect(jsonPath("task").value("task0"))
                .andExpect(jsonPath("completed").value(false));

    }

    @Test
    public void shouldUpdateExistingTodoById() throws Exception {
        //arrange
        Todo task = todoRepository.save(new Todo(null, "task", false));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/todo/{id}", task.getId());
        requestBuilder.contentType(MediaType.APPLICATION_JSON);
        TodoUpdateRequest todoUpdateRequest = new TodoUpdateRequest("updated task", true);
        requestBuilder.content(objectMapper.writeValueAsString(todoUpdateRequest));
        //act
        ResultActions resultActions = mockMvc.perform(requestBuilder);
        //assert
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("id").isNotEmpty())
                .andExpect(jsonPath("task").value(todoUpdateRequest.getTask()))
                .andExpect(jsonPath("completed").value(todoUpdateRequest.isCompleted()));
        List<Todo> todoList = todoRepository.findAll();
        assertEquals(todoUpdateRequest.getTask(), todoList.get(0).getTask());
        assertEquals(todoUpdateRequest.isCompleted(), todoList.get(0).isCompleted());
    }
}
