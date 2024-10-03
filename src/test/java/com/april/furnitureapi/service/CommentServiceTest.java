package com.april.furnitureapi.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.april.furnitureapi.config.TestcontainerInitializer;
import com.april.furnitureapi.data.FurnitureRepository;
import com.april.furnitureapi.data.UserRepository;
import com.april.furnitureapi.domain.Comment;
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
class CommentServiceTest {
    @Autowired
    private CommentService commentService;
    @Autowired
    private FurnitureRepository furnitureRepository;
    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void cleanAll() {
        furnitureRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @Sql({"/users-create.sql", "/furniture-create.sql"})
    void testCreateComment() {
        var comment = new Comment();
        comment.setRating(4.5);

        comment = commentService.create(comment, "4326746", "email2@gmail.com");

        var furniture = furnitureRepository.findByVendorCode("4326746").get();
        var user = userRepository.findByEmail("email2@gmail.com").get();
        assertEquals(furniture, comment.getFurniture());
        assertEquals(user, comment.getAuthor());
        assertEquals(String.valueOf(4.5), furniture.getRating());
        assertTrue(furniture.getComments().contains(comment));
    }


    @Test
    @Sql({"/users-create.sql", "/furniture-create.sql"})
    void testUpdateRating() {
        var comment1 = new Comment();
        comment1.setRating(4.5);
        var comment2 = new Comment();
        comment2.setRating(3.0);
        var comment3 = new Comment();
        comment3.setRating(5.0);
        var comment4 = new Comment();
        comment4.setRating(1.5);

        commentService.create(comment1, "4326746", "email@gmail.com");
        commentService.create(comment2, "4326746", "email1@gmail.com");
        commentService.create(comment3, "4326746", "email2@gmail.com");
        commentService.create(comment4, "4326746", "email3@gmail.com");

        var furniture = furnitureRepository.findByVendorCode("4326746").get();
        assertEquals(4, furniture.getComments().size());
        assertEquals(3.5, Double.parseDouble(furniture.getRating()));
    }
}
