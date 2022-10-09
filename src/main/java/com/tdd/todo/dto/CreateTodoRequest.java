package com.tdd.todo.dto;

import lombok.*;

import java.io.Serializable;

/**
 * A DTO for the {@link com.tdd.todo.model.Todo} entity
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreateTodoRequest implements Serializable {
    private String task;
}