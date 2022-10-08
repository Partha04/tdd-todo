package com.tdd.todo;

import com.tdd.todo.testContainers.PostgresTestContainer;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TodoApplicationTests extends PostgresTestContainer {

    @Test
    void contextLoads() {
    }
}
