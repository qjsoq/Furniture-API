package com.april.furnitureapi.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.april.furnitureapi.config.TestcontainerInitializer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@ContextConfiguration(initializers = TestcontainerInitializer.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class RepositoryTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FurnitureRepository furnitureRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private CartRepository cartRepository;

    @Test
    @Sql("/users-create.sql")
    void findUserByEmail() {
        var user = userRepository.findByEmail("email@gmail.com");
        assertEquals("John", user.get().getName());
    }
    @Test
    @Sql({"/users-create.sql", "/furniture-create.sql", "/comments-create.sql"})
    void deleteFurniture(){
        furnitureRepository.deleteByVendorCode("4238748");

        assertTrue(userRepository.existsByEmailOrUsername("email6@gmail.com", "batya"));
        assertFalse(commentRepository.existsById(1L));
        assertTrue(userRepository.existsByEmailOrUsername("email@gmail.com", "qjsoq"));
    }

    @Test
    @Sql({"/users-create.sql", "/furniture-create.sql", "/cart-create.sql"})
    void addItemInCart(){
        var cart = cartRepository.findByCartCode("3141333").get();
        var furniture = furnitureRepository.findByVendorCode("4238748").get();
        var furniture2 = furnitureRepository.findByVendorCode("1235673").get();
        cart.addItem(furniture);
        cart.addItem(furniture2);

        assertEquals((long) cart.getPrice(), furniture2.getPrice() + furniture.getPrice());
        assertTrue(cart.getItems().containsKey(furniture));
    }

    @Test
    @Sql({"/users-create.sql", "/furniture-create.sql", "/cart-create.sql"})
    void deleteCartDoesNotDeleteFurniture(){
        var cart = cartRepository.findByCartCode("3141333").get();
        var furniture = furnitureRepository.findByVendorCode("4238748").get();
        cart.addItem(furniture);

        cartRepository.deleteByCartCode("3141333");
        assertTrue(furnitureRepository.existsByVendorCode("4238748"));
        assertFalse(cartRepository.existsById(1L));
    }

    @Test
    @Sql({"/users-create.sql", "/furniture-create.sql", "/cart-create.sql"})
    void deleteItemFromCart(){
        var cart = cartRepository.findByCartCode("3141333").get();
        var furniture = furnitureRepository.findByVendorCode("4238748").get();
        cart.addItem(furniture);

        cart.removeItem(furniture);
        assertEquals(0, cart.getPrice());
        assertFalse(cart.getItems().containsKey(furniture));
    }

}