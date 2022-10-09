package com.tdd.todo.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * A DTO for the {@link com.tdd.todo.model.Todo} entity
 */
@Data
public class CreateTodoDto implements Serializable {
    private final String task;
}