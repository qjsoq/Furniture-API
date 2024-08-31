package com.april.furnitureapi.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.april.furnitureapi.config.TestcontainerInitializer;
import com.april.furnitureapi.data.FurnitureRepository;
import com.april.furnitureapi.data.UserRepository;
import com.april.furnitureapi.data.WarehouseRepository;
import com.april.furnitureapi.domain.Address;
import com.april.furnitureapi.domain.Availability;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = TestcontainerInitializer.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class WarehouseServiceTest {
    @Autowired
    private WarehouseService warehouseService;
    @Autowired
    private FurnitureRepository furnitureRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private WarehouseRepository warehouseRepository;

    @AfterEach
    void cleanAll() {
        warehouseRepository.deleteAll();
        furnitureRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @Sql({"/users-create.sql", "/furniture-create.sql", "/warehouse-create.sql"})
    void testAddFurniture() {
        var warehouse = warehouseService.addFurniture(2L, "8563276", 10);
        var furniture = furnitureRepository.findByVendorCode("8563276").get();

        assertEquals(Availability.INSTOCK, furniture.getAvailability());


        assertEquals(1, warehouse.getStorage().size());
        assertEquals(10, warehouse.getStorage().get(furniture));
    }

    @Test
    @Sql({"/users-create.sql", "/furniture-create.sql", "/warehouse-create.sql",
            "/warehouse-storage-create.sql"})
    void testGetWarehouseWithFurniture() {
        var furniture = furnitureRepository.findById(1L).get();
        var warehouse = warehouseService.getWarehouseWithFurniture(furniture, 5);

        assertTrue(warehouse.isPresent());
        assertEquals(Address.FONTENOY_STR, warehouse.get().getAddress());
        assertThrowsExactly(NoSuchElementException.class,
                () -> warehouseService.getWarehouseWithFurniture(furniture, 11).get());
    }

}