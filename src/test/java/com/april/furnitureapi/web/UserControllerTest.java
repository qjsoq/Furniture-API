package com.april.furnitureapi.web;

import static com.april.furnitureapi.utils.JsonUtils.contentListMatcher;
import static com.april.furnitureapi.utils.JsonUtils.contentMatcher;
import static com.april.furnitureapi.web.WebConstants.API;
import static com.april.furnitureapi.web.WebConstants.USERS;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.april.furnitureapi.config.TestcontainerInitializer;
import com.april.furnitureapi.data.CartRepository;
import com.april.furnitureapi.data.UserRepository;
import com.april.furnitureapi.web.dto.cart.CartDto;
import com.april.furnitureapi.web.dto.user.UserCreationDto;
import com.april.furnitureapi.web.mapper.CartMapper;
import com.april.furnitureapi.web.mapper.Usermapper;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = TestcontainerInitializer.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UserControllerTest {
    private final String url = API + USERS;
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CartMapper cartMapper;
    @Autowired
    private Usermapper usermapper;

    @AfterEach
    void cleanAll() {
        cartRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @Sql({"/users-create.sql", "/cart-create.sql"})
    @WithMockUser("email@gmail.com")
    void testGetMyOrders() throws Exception {
        List<CartDto> cartDtoList = cartRepository.findAll().stream()
                .filter((temp) -> temp.getCreator().getActualUsername().equals("qjsoq"))
                .map(cartMapper::toDto)
                .toList();

        mockMvc.perform(get(url + "/myOrders"))
                .andExpect(status().isOk())
                .andExpect(contentListMatcher(cartDtoList));
    }

    @Test
    @Sql("/users-create.sql")
    @WithMockUser("email@gmail.com")
    void testUpdateSelf() throws Exception {
        var user = new UserCreationDto();
        user.setName("Carlos");
        user.setUsername("qjsoq");
        user.setEmail("email@gmail.com");
        user.setLastname("Lastname");
        user.setPassword("123456");

        var response = mockMvc.perform(patch(url + "/self")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)));

        var updatedUser = usermapper.toPayload(userRepository.findByEmail("email@gmail.com").get());
        response.andExpectAll(status().isOk(),
                jsonPath("$.name").value("Carlos"),
                contentMatcher(updatedUser));
    }
}