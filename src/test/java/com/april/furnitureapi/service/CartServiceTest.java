package com.april.furnitureapi.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.april.furnitureapi.config.TestcontainerInitializer;
import com.april.furnitureapi.data.CartRepository;
import com.april.furnitureapi.data.FurnitureRepository;
import com.april.furnitureapi.data.UserRepository;
import com.april.furnitureapi.data.WarehouseRepository;
import com.april.furnitureapi.domain.Availability;
import com.april.furnitureapi.exception.CartNotFoundException;
import com.april.furnitureapi.exception.FurnitureNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = TestcontainerInitializer.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class CartServiceTest {
    @Autowired
    private CartService cartService;
    @Autowired
    private FurnitureRepository furnitureRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private WarehouseRepository warehouseRepository;

    @AfterEach
    void cleanAll() {
        warehouseRepository.deleteAll();
        cartRepository.deleteAll();
        furnitureRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @Sql({"/users-create.sql", "/furniture-create.sql"})
    void testAddAndCreate() {
        var cart = cartService.addAndCreateCart("4326746", "email1@gmail.com");
        var furniture = furnitureRepository.findByVendorCode("4326746").get();
        var user = userRepository.findByEmail("email1@gmail.com").get();

        assertEquals(90000, cart.getPrice());
        assertTrue(cart.getItems().containsKey(furniture));
        assertEquals(user, cart.getCreator());
    }

    @Test
    @Sql({"/users-create.sql", "/furniture-create.sql", "/warehouse-create.sql",
            "/warehouse-storage-create.sql", "/cart-create.sql"})
    void testAddToCart() {
        var furniture = furnitureRepository.findByVendorCode("1235673").get();
        var cart = cartService.addAndCreateCart("1235673", "email@gmail.com");
        cart = cartService.addToCart(cart, "1235673");
        var cart2 = cartRepository.findByCartCode("3141333").get();

        assertEquals(15600, cart.getPrice());
        assertEquals(2, cart.getItems().get(furniture));
        assertThrowsExactly(FurnitureNotFoundException.class,
                () -> cartService.addToCart(cart2, "4238748"));
    }

    @Test
    @Sql({"/users-create.sql", "/furniture-create.sql", "/warehouse-create.sql",
            "/warehouse-storage-create.sql", "/cart-create.sql"})
    void testRemoveItem() {
        var cart = cartRepository.findByCartCode("3141333").get();
        var furniture = furnitureRepository.findByVendorCode("1235673").get();
        cart = cartService.addToCart(cart, "1235673");
        cart = cartService.addToCart(cart, "9543422");

        cart = cartService.deleteFromCart(cart, "1235673");

        assertFalse(cart.getItems().containsKey(furniture));
        assertEquals(15000, cart.getPrice());
    }

    @Test
    @Sql({"/users-create.sql", "/furniture-create.sql", "/warehouse-create.sql",
            "/warehouse-storage-create.sql", "/cart-create.sql"})
    void testCheckout() {
        var cart = cartService.addAndCreateCart("1235673", "email@gmail.com");
        var varcode = cart.getCartCode();
        var taburetka = furnitureRepository.findByVendorCode("1235673").get();
        cart = cartService.addToCart(cart, "9543422");
        cart = cartService.addToCart(cart, "4326746");

        cart = cartService.checkout(cart);

        var warehouse = warehouseRepository.findById(2L).get();
        var stol = furnitureRepository.findByVendorCode("4326746").get();
        assertEquals(9, warehouse.getStorage().get(taburetka));
        assertEquals(0, warehouse.getStorage().get(stol));
        assertEquals(Availability.OUTSTOCK, stol.getAvailability());
        assertEquals(112800, cart.getPrice());
        assertTrue(cartRepository.findByCartCode(varcode).isPresent());
    }

    @Test
    @Sql({"/users-create.sql", "/furniture-create.sql", "/cart-create.sql",
            "/cart-content-create.sql"})
    void testDeleteNonExistentCart() {
        assertThrowsExactly(CartNotFoundException.class, () -> cartService.deleteCart("1234567"));
    }
}