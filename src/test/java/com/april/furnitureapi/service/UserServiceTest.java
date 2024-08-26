package com.april.furnitureapi.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.april.furnitureapi.config.TestcontainerInitializer;
import com.april.furnitureapi.data.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = TestcontainerInitializer.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserServiceTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;

    @Test
    @Sql({"/users-create.sql"})
    void updateUser() {
        String newLastName = "Soryu";
        var user = userService.findByEmail("email1@gmail.com");

        user.setLastname(newLastName);
        userService.updateUser(user);
        assertEquals(userRepository.findByEmail("email1@gmail.com").get().getLastname(),
                newLastName);
    }
}