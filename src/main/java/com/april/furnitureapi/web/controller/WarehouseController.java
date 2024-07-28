package com.april.furnitureapi.web.controller;

import com.april.furnitureapi.service.WarehouseService;
import com.april.furnitureapi.web.dto.warehouse.WarehouseDto;
import com.april.furnitureapi.web.mapper.WarehouseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.april.furnitureapi.web.WebConstants.API;

@RestController
@RequestMapping(API + "/warehouse")
@RequiredArgsConstructor
public class WarehouseController {
    private final WarehouseService warehouseService;
    private final WarehouseMapper warehouseMapper;
    @GetMapping
    public ResponseEntity<List<WarehouseDto>> getAllWarehouses(){
        return ResponseEntity.ok(warehouseService.getAll().stream()
                .map(warehouseMapper::toDto)
                .toList());
    }
}
