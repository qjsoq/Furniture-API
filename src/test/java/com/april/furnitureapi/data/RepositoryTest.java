package com.april.furnitureapi.data;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.april.furnitureapi.config.TestcontainerInitializer;
import com.april.furnitureapi.domain.Comment;
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
    @Autowired
    private WarehouseRepository warehouseRepository;

    @Test
    @Sql("/users-create.sql")
    void findUserByEmail() {
        var user = userRepository.findByEmail("email@gmail.com");
        assertEquals("John", user.get().getName());
    }

    @Test
    @Sql({"/users-create.sql", "/furniture-create.sql", "/comments-create.sql"})
    void deleteFurniture() {
        furnitureRepository.deleteByVendorCode("4238748");

        assertTrue(userRepository.existsByEmailOrUsername("email6@gmail.com", "batya"));
        assertFalse(commentRepository.existsById(1L));
        assertTrue(userRepository.existsByEmailOrUsername("email@gmail.com", "qjsoq"));
    }
    @Test
    @Sql({"/users-create.sql", "/cart-create.sql"})
    void testUserCart(){
        var user = userRepository.findByEmail("email@gmail.com").get();

        assertSame(cartRepository.findByCartCode("3141333").get().getCreator(), user);
    }

    @Test
    @Sql({"/users-create.sql", "/furniture-create.sql", "/cart-create.sql"})
    void addItemInCart() {
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
    void deleteCartDoesNotDeleteFurnitureAndUser() {
        var cart = cartRepository.findByCartCode("3141333").get();
        var furniture = furnitureRepository.findByVendorCode("4238748").get();
        cart.addItem(furniture);

        cartRepository.deleteByCartCode("3141333");
        assertTrue(furnitureRepository.existsByVendorCode("4238748"));
        assertTrue(userRepository.existsByEmailOrUsername("email@gmail.com", "qjsoq"));
        assertFalse(cartRepository.existsById(1L));
    }

    @Test
    @Sql({"/users-create.sql", "/furniture-create.sql", "/cart-create.sql"})
    void deleteItemFromCart() {
        var cart = cartRepository.findByCartCode("3141333").get();
        var furniture = furnitureRepository.findByVendorCode("4238748").get();
        cart.addItem(furniture);

        cart.removeItem(furniture);
        assertEquals(0, cart.getPrice());
        assertFalse(cart.getItems().containsKey(furniture));
    }

    @Test
    @Sql({"/users-create.sql", "/furniture-create.sql", "/comments-create.sql"})
    void checkComments() {
        var comment = commentRepository.findById(1L).get();
        var comment2 = commentRepository.findById(3L).get();
        var furniture = furnitureRepository.findByVendorCode("4238748").get();

        assertArrayEquals(furniture.getComments().toArray(new Comment[0]),
                new Comment[] {comment, comment2});
    }

    @Test
    @Sql({"/users-create.sql", "/furniture-create.sql", "/comments-create.sql"})
    void deleteCommentDoesNotDeleteFurniture() {
        commentRepository.deleteById(1L);

        assertTrue(furnitureRepository.existsById(2L));
    }

    @Test
    @Sql({"/users-create.sql", "/furniture-create.sql", "/warehouse-create.sql"})
    void addItemToWarehouse(){
        var warehouse = warehouseRepository.findById(2L).get();
        var furniture = furnitureRepository.findByVendorCode("4238748").get();

        warehouse.addFurniture(furniture, 5);
        warehouse.addFurniture(furniture, 5);
        assertEquals(10, warehouseRepository.findById(2L).get().getStorage().get(furniture));
    }
}