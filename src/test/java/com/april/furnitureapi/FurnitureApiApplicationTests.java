package com.april.furnitureapi;

import com.april.furnitureapi.config.TestcontainerInitializer;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(initializers = TestcontainerInitializer.class)
@ActiveProfiles("test")
class FurnitureApiApplicationTests {

    @Test
    void contextLoads() {
    }

}
