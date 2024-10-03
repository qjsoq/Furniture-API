package com.april.furnitureapi.web;

import static com.april.furnitureapi.web.WebConstants.API;
import static com.april.furnitureapi.web.WebConstants.AUTH;
import static com.april.furnitureapi.web.WebConstants.SIGN_IN;
import static com.april.furnitureapi.web.WebConstants.SIGN_UP;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.april.furnitureapi.config.TestcontainerInitializer;
import com.april.furnitureapi.data.ConfirmationRepository;
import com.april.furnitureapi.data.UserRepository;
import com.april.furnitureapi.service.UserService;
import com.april.furnitureapi.web.dto.auth.AuthenticationRequest;
import com.april.furnitureapi.web.dto.user.UserCreationDto;
import com.icegreen.greenmail.configuration.GreenMailConfiguration;
import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.ServerSetupTest;
import java.util.stream.Stream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = TestcontainerInitializer.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthControllerTest {
    @RegisterExtension
    static GreenMailExtension greenMail =
            new GreenMailExtension(ServerSetupTest.SMTP)
                    .withConfiguration(
                            GreenMailConfiguration.aConfig().withUser("dima", "test"))
                    .withPerMethodLifecycle(true);
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String url = API + AUTH;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserService userService;
    @Autowired
    private ConfirmationRepository confirmationRepository;
    @Autowired
    private UserRepository userRepository;

    private static Stream<Arguments> testForbidToCreateUserWithInvalidOrOccupiedCredentials() {
        return Stream.of(
                Arguments.of("Name", "Lastname", "notUsedUsername", "123456", "email@gmail.com"),
                Arguments.of("Name", "Lastname", "qjsoq", "123456", "notusedemail@gmail.com"),
                Arguments.of("Name", "Lastname", "qjsoq2", "123456", "email1@gmail.com"),
                Arguments.of("N", "Lastname", "notUsedUsername", "123456",
                        "notusedemail@gmail.com"),
                Arguments.of("Name", "L", "notUsedUsername", "123456", "notusedemail@gmail.com"),
                Arguments.of("Name", "Lastname", "q", "123456", "notusedemail@gmail.com"),
                Arguments.of("Name", "Lastname", "notUsedUsername", "1234",
                        "notusedemail@gmail.com"),
                Arguments.of("Name", "Lastname", "notUsedUsername", "123456", "email1"),
                Arguments.of("Name", "Lastname", "notUsedUsername;@", "123456",
                        "notusedemail@gmail.com"),
                Arguments.of("Name", "Laсtname", "notUsedUsername", "123456",
                        "notusedemail@gmail.com"),
                Arguments.of("", "", "", "", "")
        );
    }

    private static UserCreationDto getValidUser() {
        var newUser = new UserCreationDto();
        newUser.setEmail("notused@email.com");
        newUser.setPassword("123456");
        newUser.setUsername("notUsedUsername");
        newUser.setName("Name");
        newUser.setLastname("Lastname");
        return newUser;
    }

    @AfterEach
    void cleanAll() {
        confirmationRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void testSignUp() throws Exception {
        var newUser = getValidUser();

        var newUserInJson = objectMapper.writeValueAsString(newUser);

        mockMvc.perform(post(url + "/" + SIGN_UP)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newUserInJson))
                .andExpectAll(status().isCreated(),
                        jsonPath("$.email").value("notused@email.com"));

        assertTrue(userService.findByEmail("notused@email.com").isEnabled());
    }

    @ParameterizedTest
    @MethodSource
    @Sql("/users-create.sql")
    void testForbidToCreateUserWithInvalidOrOccupiedCredentials(String name, String lastname,
                                                                String username, String password,
                                                                String email)
            throws Exception {
        var newUser = new UserCreationDto();
        newUser.setName(name);
        newUser.setLastname(lastname);
        newUser.setUsername(username);
        newUser.setPassword(password);
        newUser.setEmail(email);

        mockMvc.perform(post(url + "/" + SIGN_UP)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newUser)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Sql("/users-create.sql")
    void testSignIn() throws Exception {
        var authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setEmail("email@gmail.com");
        authenticationRequest.setPassword("123456");

        mockMvc.perform(post(url + "/" + SIGN_IN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authenticationRequest)))
                .andExpectAll(status().is2xxSuccessful(),
                        jsonPath("$.token", notNullValue()));
    }

    @Test
    @Sql("/users-create.sql")
    void testForbidSignInNonExistentUser() throws Exception {
        var authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setEmail("nonExistentEmail@gmail.com");
        authenticationRequest.setPassword("123456");

        var authenticationResponse = mockMvc.perform(post(url + "/" + SIGN_IN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authenticationRequest)));

        authenticationResponse.andExpectAll(status().isNotFound(),
                jsonPath("$.message").value(
                        "User with email nonExistentEmail@gmail.com doesn`t exist"));
    }

    @Test
    void testVerifyToken() throws Exception {
        var newUser = getValidUser();

        mockMvc.perform(post(url + "/" + SIGN_UP)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newUser)));

        var confirmationToken = confirmationRepository.findAll().get(0).getToken();

        mockMvc.perform(get(url + "/?token=" + confirmationToken))
                .andExpectAll(status().is2xxSuccessful(),
                        jsonPath("$.message").value("Your account was successfully verified!"),
                        jsonPath("$.verified").value(true));
        assertTrue(userService.findByEmail("notused@email.com").isVerified());
    }


}