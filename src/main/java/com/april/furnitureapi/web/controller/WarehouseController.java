package com.april.furnitureapi.web.controller;

import static com.april.furnitureapi.web.WebConstants.API;
import static com.april.furnitureapi.web.WebConstants.WAREHOUSE;

import com.april.furnitureapi.service.WarehouseService;
import com.april.furnitureapi.web.dto.warehouse.WarehouseDetailedDto;
import com.april.furnitureapi.web.dto.warehouse.WarehouseDto;
import com.april.furnitureapi.web.mapper.WarehouseMapper;
import java.net.URI;
import java.security.Principal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = API + WAREHOUSE, produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class WarehouseController {
    private final WarehouseService warehouseService;
    private final WarehouseMapper warehouseMapper;

    @GetMapping
    public ResponseEntity<List<WarehouseDto>> getAllWarehouses() {
        return ResponseEntity.ok(warehouseService.getAll().stream()
                .map(warehouseMapper::toDto)
                .toList());
    }

    @PutMapping("/{id}/{vendorCode}/{amount}")
    @PreAuthorize("@furnitureChecker.checkId(#vendorCode) and hasRole('ROLE_ADMIN')")
    public ResponseEntity<WarehouseDetailedDto> addFurnitureToWarehouse(@PathVariable Long id,
                                                                        @PathVariable
                                                                        String vendorCode,
                                                                        @PathVariable
                                                                        Integer amount,
                                                                        Principal principal) {
        var warehouse = warehouseService.addFurniture(id, vendorCode, amount);
        return ResponseEntity.created(URI.create("")).body(
                warehouseMapper.toDetailedDto(warehouse)
        );
    }
}
