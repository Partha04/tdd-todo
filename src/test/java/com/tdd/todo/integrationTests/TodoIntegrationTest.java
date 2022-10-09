package com.tdd.todo.integrationTests;

import com.tdd.todo.dto.CreateTodoRequest;
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
        CreateTodoRequest createTodoRequest = new CreateTodoRequest("New task1");
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/todo");
        requestBuilder.contentType(MediaType.APPLICATION_JSON);
        requestBuilder.content(objectMapper.writeValueAsString(createTodoRequest));
        //act
        ResultActions resultActions = mockMvc.perform(requestBuilder);
        //assert
        resultActions.andExpect(status().isCreated()).andExpect(jsonPath("id").isNotEmpty())
                .andExpect(jsonPath("task").value(createTodoRequest.getTask()))
                .andExpect(jsonPath("completed").value(false));
        List<Todo> todoList = todoRepository.findAll();
        assertEquals(createTodoRequest.getTask(), todoList.get(0).getTask());

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
}
