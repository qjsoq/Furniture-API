package com.april.furnitureapi.data;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.april.furnitureapi.config.TestcontainerInitializer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@ContextConfiguration(initializers = TestcontainerInitializer.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class RepositoryTest {
    @Autowired
    private UserRepository userRepository;


    @Test
    @Sql("/users-create.sql")
    void findUserByEmail() {
        var user = userRepository.findByEmail("email@gmail.com");
        assertTrue(user.isPresent());
    }
}