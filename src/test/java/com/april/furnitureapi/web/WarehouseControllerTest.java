package com.april.furnitureapi.web;

import static com.april.furnitureapi.web.WebConstants.API;
import static com.april.furnitureapi.web.WebConstants.WAREHOUSE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.april.furnitureapi.config.TestcontainerInitializer;
import com.april.furnitureapi.data.FurnitureRepository;
import com.april.furnitureapi.data.UserRepository;
import com.april.furnitureapi.data.WarehouseRepository;
import com.april.furnitureapi.web.mapper.WarehouseMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = TestcontainerInitializer.class)
@AutoConfigureMockMvc
class WarehouseControllerTest {
    private final String url = API + WAREHOUSE;
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FurnitureRepository furnitureRepository;
    @Autowired
    private WarehouseRepository warehouseRepository;
    @Autowired
    private WarehouseMapper warehouseMapper;

    @AfterEach
    void cleanAll() {
        warehouseRepository.deleteAll();
        furnitureRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @Sql({"/users-create.sql", "/furniture-create.sql", "/warehouse-create.sql"})
    @WithMockUser(roles = "ADMIN")
    void testAddFurnitureToWarehouse() throws Exception {
        var warehouseResponseDto =
                mockMvc.perform(put(url + "/{id}/{vendorCode}/{amount}", 2, "4329789", 5))
                        .andExpect(status().isCreated())
                        .andReturn();

        var expectedWarehouse = objectMapper.writeValueAsString(
                warehouseMapper.toDetailedDto(warehouseRepository.findById(2L).get()));
        var content = warehouseResponseDto.getResponse().getContentAsString();
        assertEquals(objectMapper.readTree(expectedWarehouse), objectMapper.readTree(content));
    }
}