package com.april.furnitureapi.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.april.furnitureapi.config.TestcontainerInitializer;
import com.april.furnitureapi.data.FurnitureRepository;
import com.april.furnitureapi.data.UserRepository;
import com.april.furnitureapi.data.WarehouseRepository;
import com.april.furnitureapi.domain.Availability;
import com.april.furnitureapi.domain.Furniture;
import com.april.furnitureapi.domain.FurnitureCategory;
import com.april.furnitureapi.domain.FurnitureDomain;
import com.april.furnitureapi.exception.FurnitureNotFoundException;
import com.april.furnitureapi.exception.WarehouseNotFoundException;
import java.util.Optional;
import java.util.stream.Stream;
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
class FurnitureServiceTest {
    @Autowired
    private FurnitureService furnitureService;
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
    @Sql({"/users-create.sql", "/furniture-create.sql"})
    void testGetFurniture() {
        var furniture = furnitureRepository.findByVendorCode("4326746").get();

        assertEquals(furniture, furnitureService.findByVendorCode("4326746"));
        assertThrowsExactly(FurnitureNotFoundException.class,
                () -> furnitureService.findByVendorCode("3273836"));
    }

    @Test
    @Sql({"/users-create.sql", "/furniture-create.sql"})
    void testGetByDomainAndCategory() {
        var furnitureSortedByNovelty =
                furnitureService.findByDomainAndCategory(FurnitureCategory.TABLE,
                        FurnitureDomain.KITCHEN, Optional.empty());
        var furnitureSortedFromCheapToExpensive =
                furnitureService.findByDomainAndCategory(FurnitureCategory.TABLE,
                        FurnitureDomain.KITCHEN, Optional.of("cheap"));
        var furnitureSortedFromExpensiveToCheap =
                furnitureService.findByDomainAndCategory(FurnitureCategory.TABLE,
                        FurnitureDomain.KITCHEN, Optional.of("expensive"));


        assertEquals(4,
                (furnitureSortedByNovelty.size() & furnitureSortedFromCheapToExpensive.size()
                        &
                        furnitureSortedFromExpensiveToCheap.size()));
        Stream.of(furnitureSortedByNovelty, furnitureSortedFromCheapToExpensive,
                        furnitureSortedFromExpensiveToCheap)
                .map(list -> list.get(3).getAvailability())
                .forEach((availability -> assertEquals(Availability.OUTSTOCK, availability)));
        assertEquals("Stol4", furnitureSortedByNovelty.get(0).getTitle());
        assertEquals("Stol1", furnitureSortedByNovelty.get(2).getTitle());
        assertEquals("Stol2", furnitureSortedFromExpensiveToCheap.get(0).getTitle());
        assertEquals("Stol4", furnitureSortedFromExpensiveToCheap.get(1).getTitle());
        assertEquals("Stol1", furnitureSortedFromCheapToExpensive.get(0).getTitle());
        assertEquals("Stol2", furnitureSortedFromCheapToExpensive.get(2).getTitle());
    }

    @Test
    @Sql({"/users-create.sql", "/warehouse-create.sql", "/furniture-create.sql",
            "/warehouse-storage-create.sql"})
    void testSaveFurniture() {
        var newFurniture = new Furniture();
        var newFurniture2 = new Furniture();

        newFurniture = furnitureService.saveFurniture(newFurniture, "email6@gmail.com", 5, 2L);
        newFurniture2 = furnitureService.saveFurniture(newFurniture2, "email6@gmail.com", 0, 2L);

        var warehouse = warehouseRepository.findById(2L).get();
        var user = userRepository.findByEmail("email6@gmail.com").get();
        assertEquals(Availability.INSTOCK, newFurniture.getAvailability());
        assertEquals(Availability.ONCOMING, newFurniture2.getAvailability());
        assertEquals(user, newFurniture.getCreator());
        assertTrue(furnitureRepository.findById(9L).isPresent());
        assertTrue(warehouse.getStorage().containsKey(newFurniture));
    }

    @Test
    void testShouldNotSaveFurniture() {
        var newFurniture = new Furniture();

        assertThrowsExactly(WarehouseNotFoundException.class,
                () -> furnitureService.saveFurniture(newFurniture, "email6@gmail.com", 5, 2L));
    }

    @Test
    @Sql({"/users-create.sql", "/furniture-create.sql"})
    void testUpdateFurniture() {
        var furniture = furnitureService.findByVendorCode("4238748");

        furniture.setPrice(1000L);
        furnitureService.update(furniture);

        var updatedFurniture = furnitureService.findByVendorCode("4238748");
        assertEquals(furniture.getId(), updatedFurniture.getId());
        assertEquals(1000L, (long) updatedFurniture.getPrice());
    }

}