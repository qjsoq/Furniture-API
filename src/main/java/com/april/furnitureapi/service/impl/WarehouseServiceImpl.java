package com.april.furnitureapi.service.impl;

import com.april.furnitureapi.data.FurnitureRepository;
import com.april.furnitureapi.data.WarehouseRepository;
import com.april.furnitureapi.domain.Warehouse;
import com.april.furnitureapi.exception.FurnitureNotFoundException;
import com.april.furnitureapi.exception.WarehouseNotFoundException;
import com.april.furnitureapi.service.WarehouseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WarehouseServiceImpl implements WarehouseService {
    private final WarehouseRepository warehouseRepository;
    private final FurnitureRepository furnitureRepository;
    @Override
    public Warehouse save(Warehouse warehouse) {
        return warehouseRepository.save(warehouse);
    }

    @Override
    public List<Warehouse> getAll() {
        return warehouseRepository.findAll();
    }

    @Override
    public Warehouse addFurniture(Long id, String vendorCode, Integer amount) {
        var warehouse = warehouseRepository.findById(id).orElseThrow(() -> new WarehouseNotFoundException(
                "Warehouse with this id %s not found".formatted(id)
        ));
        var furniture = furnitureRepository.findByVendorCode(vendorCode).get();
        warehouse.getStorage().put(furniture, warehouse.getStorage().get(furniture) + amount);
        warehouseRepository.save(warehouse);
        return warehouse;
    }
}
