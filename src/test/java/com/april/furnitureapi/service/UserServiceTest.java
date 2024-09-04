package com.april.furnitureapi.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.april.furnitureapi.config.TestcontainerInitializer;
import com.april.furnitureapi.data.CartRepository;
import com.april.furnitureapi.data.ConfirmationRepository;
import com.april.furnitureapi.data.UserRepository;
import com.april.furnitureapi.domain.User;
import com.april.furnitureapi.exception.UserAlreadyExistsException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = TestcontainerInitializer.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserServiceTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private ConfirmationRepository confirmationRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder encoder;

    @AfterEach
    void cleanAll() {
        confirmationRepository.deleteAll();
        cartRepository.deleteAll();
        userRepository.deleteAll();
    }

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

    @Test
    @Sql({"/users-create.sql"})
    void shouldNotUpdateUser() {
        String email = "email@gmail.com";
        String username = "qjsoq5";
        var user = userService.findByEmail("email1@gmail.com");
        var user2 = userService.findByEmail("email2@gmail.com");
       
        user2.setEmail(email);
        user.setUsername(username);
        
        assertThrowsExactly(UserAlreadyExistsException.class, () -> userService.updateUser(user));
        assertThrowsExactly(UserAlreadyExistsException.class, () -> userService.updateUser(user2));
    }

    @Test
    void createUser() {
        var user = new User();
        user.setUsername("qjsoq6");
        user.setName("Name");
        user.setLastname("Lastname");
        user.setPassword("123456");
        user.setEmail("gmail@gmail.com");

        userService.signUp(user);
      
        var confirmation = confirmationRepository.findAll().get(0);

        assertTrue(userRepository.findByEmail("gmail@gmail.com").isPresent());
        assertTrue(encoder.matches("123456",
                userRepository.findByEmail("gmail@gmail.com").get().getPassword()));
        assertEquals(user, confirmation.getUser());
    }

    @Test
    @Sql({"/users-create.sql", "/cart-create.sql"})
    void testGetUsersOrders() {
        var userOrders = userService.getMyOrders("email@gmail.com");

        assertEquals(3, userOrders.size());
        assertEquals("3141333", userOrders.get(0).getCartCode());
    }
}
