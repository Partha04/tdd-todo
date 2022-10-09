package com.tdd.todo.dto;

import lombok.*;

import java.io.Serializable;
import java.util.UUID;

/**
 * A DTO for the {@link com.tdd.todo.model.Todo} entity
 */
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class TodoResponse implements Serializable {
    private UUID id;
    private String task;
    private boolean completed;
}