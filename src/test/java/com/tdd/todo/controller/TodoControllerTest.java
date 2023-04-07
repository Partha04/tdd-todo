package com.tdd.todo.controller;

import com.tdd.todo.dto.TodoCreateRequest;
import com.tdd.todo.dto.TodoResponse;
import com.tdd.todo.dto.TodoUpdateRequest;
import com.tdd.todo.exception.EntityNotFoundException;
import com.tdd.todo.service.TodoService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest
class TodoControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    TodoService todoService;
    ObjectMapper mapper = new ObjectMapper();

    @Nested
    class CreateTodoTests {
        @Test
        void addTodoTaskSuccessGivesStatusCreated() throws Exception {
            //arrange
            MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/todo");
            requestBuilder.contentType(MediaType.APPLICATION_JSON);
            requestBuilder.content(mapper.writeValueAsString(new TodoCreateRequest("task")));
            //act
            ResultActions resultActions = mockMvc.perform(requestBuilder);
            //assert
            resultActions.andExpect(status().isCreated());
        }

        @Test
        void addTodoInvokesTheTodoService_addTodoMethod() throws Exception {
            //arrange
            when(todoService.addTodo(any(TodoCreateRequest.class))).thenReturn(new TodoResponse(UUID.randomUUID(), "new task", false));
            MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/todo");
            requestBuilder.contentType(MediaType.APPLICATION_JSON);
            requestBuilder.content(mapper.writeValueAsString(new TodoCreateRequest("task")));
            //act
            ResultActions resultActions = mockMvc.perform(requestBuilder);
            //assert
            resultActions.andExpect(jsonPath("id").isNotEmpty())
                    .andExpect(jsonPath("task").value("new task"))
                    .andExpect(jsonPath("completed").value(false));
            verify(todoService, times(1)).addTodo(any(TodoCreateRequest.class));
        }
    }

    @Nested
    class GetTodoTests {
        @Test
        void getAllTodosSuccessGivesStatusOk() throws Exception {
            //arrange
            MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/todo");
            //act
            ResultActions resultActions = mockMvc.perform(requestBuilder);
            //assert
            resultActions.andExpect(status().isOk());
        }

        @Test
        void getAllTodosShouldInvokeTodoService_getAllMethod() throws Exception {
            //arrange
            TodoResponse todoResponse = new TodoResponse(UUID.randomUUID(), "Task", false);
            when(todoService.getAllTodo()).thenReturn(List.of(todoResponse));
            MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/todo");
            //act
            ResultActions resultActions = mockMvc.perform(requestBuilder);
            //assert
            resultActions.andExpect(jsonPath("$[0].id").isNotEmpty())
                    .andExpect(jsonPath("$[0].task").value(todoResponse.getTask()))
                    .andExpect(jsonPath("$[0].completed").value(false));

            verify(todoService, times(1)).getAllTodo();
        }

        @Test
        void getAllTodosGivesAll10Todos() throws Exception {
            //arrange
            List<TodoResponse> todoResponseList = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                todoResponseList.add(new TodoResponse(UUID.randomUUID(), "Task" + i, false));
            }
            when(todoService.getAllTodo()).thenReturn(todoResponseList);
            MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/todo");
            //act
            ResultActions resultActions = mockMvc.perform(requestBuilder);
            //assert
            resultActions.andExpect(content().json(mapper.writeValueAsString(todoResponseList)));
            verify(todoService, times(1)).getAllTodo();
        }

    }

    @Nested
    class GetTodoByIDTests {
        @Test
        void getATodoByIDSuccessGivesStatusOk() throws Exception {
            //arrange
            UUID id = UUID.randomUUID();
            MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/todo/{id}", id);
            //act
            ResultActions resultActions = mockMvc.perform(requestBuilder);
            //assert
            resultActions.andExpect(status().isOk());
        }

        @Test
        void getATodoByIDSuccessInvokesTodoService_getTodoByIDMethod() throws Exception {
            //arrange
            UUID id = UUID.randomUUID();
            when(todoService.getTodoByID(id)).thenReturn(new TodoResponse(id, "Task", false));
            MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/todo/{id}", id);
            //act
            ResultActions resultActions = mockMvc.perform(requestBuilder);
            //assert
            resultActions.andExpect(status().isOk())
                    .andExpect(jsonPath("id").value(id.toString()))
                    .andExpect(jsonPath("task").value("Task"))
                    .andExpect(jsonPath("completed").value(false))
            ;
            verify(todoService, times(1)).getTodoByID(id);
        }

        @Test
        void getATodoByIDFailsForInvalidId() throws Exception {
            //arrange
            UUID id = UUID.randomUUID();
            when(todoService.getTodoByID(id)).thenThrow(new EntityNotFoundException("Task not found"));
            MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/todo/{id}", id);
            //act
            ResultActions resultActions = mockMvc.perform(requestBuilder);
            //assert
            resultActions.andExpect(status().isNotFound())
                    .andExpect(jsonPath("message").value("Task not found"));
        }

    }

    @Nested
    class UpdateTodoByIDTests {

        @Test
        void updateTodoTaskSuccessGivesStatusOk() throws Exception {
            //arrange
            UUID id = UUID.randomUUID();
            MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/todo/{id}", id);
            requestBuilder.contentType(MediaType.APPLICATION_JSON);
            requestBuilder.content(mapper.writeValueAsString(new TodoUpdateRequest("updated task", true)));
            //act
            ResultActions resultActions = mockMvc.perform(requestBuilder);
            //assert
            resultActions.andExpect(status().isOk());
        }

        @Test
        void updateTodoInvokesTheTodoService_updateTodoMethod() throws Exception {
            //arrange
            String updatedTask = "updated task";
            boolean taskCompleted = true;
            when(todoService.updateTodo(any(UUID.class), any(TodoUpdateRequest.class))).thenReturn(new TodoResponse(UUID.randomUUID(), updatedTask, taskCompleted));
            UUID id = UUID.randomUUID();
            MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/todo/{id}", id);
            requestBuilder.contentType(MediaType.APPLICATION_JSON);
            TodoUpdateRequest todoUpdateRequest = new TodoUpdateRequest(updatedTask, taskCompleted);
            requestBuilder.content(mapper.writeValueAsString(todoUpdateRequest));
            //act
            ResultActions resultActions = mockMvc.perform(requestBuilder);

            //assert
            resultActions.andExpect(jsonPath("id").isNotEmpty())
                    .andExpect(jsonPath("task").value(updatedTask))
                    .andExpect(jsonPath("completed").value(taskCompleted));

            verify(todoService, times(1)).updateTodo(any(UUID.class), any(TodoUpdateRequest.class));
        }

        @Test
        void updateATodoByIDFailsForInvalidId() throws Exception {
            //arrange
            String taskNotFound = "Task not found";
            String updatedTask = "updated task";
            boolean taskCompleted = true;
            when(todoService.updateTodo(any(UUID.class), any(TodoUpdateRequest.class))).thenThrow(new EntityNotFoundException(taskNotFound));
            UUID id = UUID.randomUUID();
            MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/todo/{id}", id);
            requestBuilder.contentType(MediaType.APPLICATION_JSON);
            TodoUpdateRequest todoUpdateRequest = new TodoUpdateRequest(updatedTask, taskCompleted);
            requestBuilder.content(mapper.writeValueAsString(todoUpdateRequest));
            //act
            ResultActions resultActions = mockMvc.perform(requestBuilder);
            //assert
            resultActions.andExpect(status().isNotFound())
                    .andExpect(jsonPath("message").value(taskNotFound));
        }
    }

    @Nested
    class DeleteTodoByIdTests {
        @Test
        void deleteTodoByIdSuccessGivesStatusOk() throws Exception {
            //arrange
            MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/todo/{id}", UUID.randomUUID());
            //act
            ResultActions resultActions = mockMvc.perform(requestBuilder);
            //assert
            resultActions.andExpect(status().isOk());
        }
    }

}
