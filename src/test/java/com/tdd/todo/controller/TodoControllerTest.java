package com.tdd.todo.controller;

import com.tdd.todo.dto.CreateTodoDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest
class TodoControllerTest {

    @Autowired
    MockMvc mockMvc;

    ObjectMapper mapper = new ObjectMapper();

    @Test
    void addTodoTaskSuccessGivesStatusCreated() throws Exception {
        //arrange
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/todo");
        requestBuilder.contentType(MediaType.APPLICATION_JSON);
        requestBuilder.content(mapper.writeValueAsString(new CreateTodoDto("task")));
        //act
        ResultActions resultActions = mockMvc.perform(requestBuilder);
        //assert
        resultActions.andExpect(status().isCreated());
    }
}