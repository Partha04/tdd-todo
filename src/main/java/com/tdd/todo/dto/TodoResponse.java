package com.tdd.todo.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

/**
 * A DTO for the {@link com.tdd.todo.model.Todo} entity
 */
@Data
public class TodoResponse implements Serializable {
    private final UUID id;
    private final String task;
    private final boolean isCompleted;
}