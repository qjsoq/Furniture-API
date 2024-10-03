package com.april.furnitureapi.web;

import static com.april.furnitureapi.web.WebConstants.API;
import static com.april.furnitureapi.web.WebConstants.CART;
import static org.hamcrest.Matchers.aMapWithSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.april.furnitureapi.config.TestcontainerInitializer;
import com.april.furnitureapi.data.CartRepository;
import com.april.furnitureapi.data.FurnitureRepository;
import com.april.furnitureapi.data.UserRepository;
import com.april.furnitureapi.data.WarehouseRepository;
import com.april.furnitureapi.service.CartService;
import com.april.furnitureapi.service.CookieService;
import com.april.furnitureapi.web.mapper.CartMapper;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
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
class CartControllerTest {
    private final String url = API + CART;
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private CartMapper cartMapper;
    @Autowired
    private CookieService cookieService;
    @Autowired
    private CartService cartService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FurnitureRepository furnitureRepository;
    @Autowired
    private WarehouseRepository warehouseRepository;
    @Autowired
    private CartRepository cartRepository;

    @AfterEach
    void cleanAll() {
        cartRepository.deleteAll();
        warehouseRepository.deleteAll();
        furnitureRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @WithMockUser("email@gmail.com")
    @Sql({"/users-create.sql", "/furniture-create.sql"})
    void testAddToCart() throws Exception {
        mockMvc.perform(put(url + "/add/{vendorCode}", "7891187"))
                .andExpectAll(status().isOk(),
                        jsonPath("$.price").value(60000),
                        jsonPath("$.cartCode").isNotEmpty(),
                        cookie().exists("cart_email_gmail.com"),
                        jsonPath(
                                "$.items..['FurnitureDto(title=Stol2, vendorCode=7891187, price=60000, availability=INSTOCK)']").value(
                                1));


    }

    @Test
    @WithMockUser("email@gmail.com")
    @Sql({"/users-create.sql", "/furniture-create.sql", "/warehouse-create.sql",
            "/warehouse-storage-create.sql"})
    void testAddToExistingCart() throws Exception {
        var newCart = cartService.addAndCreateCart("9543422", "email@gmail.com");
        var cookieCart = cookieService.getNewCookie(newCart, "email@gmail.com", 1000);
        newCart.addItem(furnitureRepository.findByVendorCode("1235673").get());
        var detailedDto = objectMapper.writeValueAsString(cartMapper.toDetailedDto(newCart));

        var jsonResponse =
                mockMvc.perform(put(url + "/add/{vendorCode}", "1235673").cookie(cookieCart))
                        .andExpectAll(status().isOk(),
                                jsonPath("$.price").value(22800),
                                jsonPath("$.items", aMapWithSize(2)))
                        .andReturn()
                        .getResponse().getContentAsString();
        assertEquals(objectMapper.readTree(detailedDto), objectMapper.readTree(jsonResponse));
    }

    @Test
    @Sql({"/users-create.sql", "/furniture-create.sql"})
    void testForbidAddingOutOfStockItems() throws Exception {
        mockMvc.perform(put(url + "/add/{vendorCode}", "4329789"))
                .andExpectAll(status().isForbidden());
    }

    @Test
    @WithMockUser("email@gmail.com")
    @Sql({"/users-create.sql", "/furniture-create.sql", "/warehouse-create.sql",
            "/warehouse-storage-create.sql"})
    void testForbidAddingDeficitItems() throws Exception {

        var cookieCart = getCookieCart("email@gmail.com", "4326746");

        mockMvc.perform(put(url + "/add/{vendorCode}", "4326746").cookie(cookieCart))
                .andExpectAll(status().isNotFound(),
                        jsonPath("$.message").value(
                                "We dont have the required amount of furniture items 4326746 in our warehouses"));
    }

    @Test
    @Sql({"/users-create.sql", "/furniture-create.sql", "/warehouse-create.sql",
            "/warehouse-storage-create.sql"})
    @WithMockUser("email1@gmail.com")
    void testSaveCart() throws Exception {
        var cookieCart = getCookieCart("email1@gmail.com", "4326746");

        mockMvc.perform(post(url + "/checkout").cookie(cookieCart))
                .andExpectAll(status().isCreated(),
                        cookie().maxAge("cart_email1_gmail.com", 0));
    }

    @Test
    @WithMockUser("email@gmail.com")
    void testForbidToSaveCartWithoutCart() throws Exception {
        mockMvc.perform(post(url + "/checkout")).andExpectAll(status().isNotFound(),
                jsonPath("$.message").value("You have not add anything to the cart"));
    }

    @Test
    @Sql({"/users-create.sql", "/cart-create.sql"})
    @WithMockUser("email@gmail.com")
    void testForbidToPersistEmptyCart() throws Exception {
        var emptyCart = cartRepository.findByCartCode("3141333").get();
        var cookieCart = cookieService.getNewCookie(emptyCart, "email@gmail.com", 1000);

        mockMvc.perform(post(url + "/checkout").cookie(cookieCart))
                .andExpectAll(status().isNotFound(),
                        jsonPath("$.message").value("Your cart is empty"));
    }

    @Test
    @Sql({"/users-create.sql", "/furniture-create.sql"})
    @WithMockUser("email@gmail.com")
    void testDeleteItemFromCart() throws Exception {
        var cookieCart = getCookieCart("email@gmail.com", "9543422");

        var deleteResponse =
                mockMvc.perform(delete(url + "/delete/{vendorCode}", "9543422").cookie(cookieCart))
                        .andExpect(status().isNoContent())
                        .andReturn().getResponse();
        var cart = cartService.decodeCartCookie(
                deleteResponse.getCookie("cart_email_gmail.com").getValue());
        assertTrue(cart.getItems().isEmpty());
        assertEquals(0, cart.getPrice());
    }

    @Test
    @Sql({"/users-create.sql", "/furniture-create.sql", "/cart-create.sql",
            "/cart-content-create.sql"})
    @WithMockUser("email1@gmail.com")
    void testDeleteOrder() throws Exception {
        mockMvc.perform(delete(url + "/{cartCode}", "2312331")).andExpect(status().isNoContent());
    }


    private Cookie getCookieCart(String email, String vendorCode) throws Exception {
        var newCart = cartService.addAndCreateCart(vendorCode, email);
        return cookieService.getNewCookie(newCart, email, 1000);
    }

}