package com.april.furnitureapi.service.impl;

import com.april.furnitureapi.data.FurnitureRepository;
import com.april.furnitureapi.data.WarehouseRepository;
import com.april.furnitureapi.domain.Availability;
import com.april.furnitureapi.domain.Furniture;
import com.april.furnitureapi.domain.Warehouse;
import com.april.furnitureapi.exception.WarehouseNotFoundException;
import com.april.furnitureapi.service.WarehouseService;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
        var warehouse =
                warehouseRepository.findById(id).orElseThrow(() -> new WarehouseNotFoundException(
                        "Warehouse with this id %s not found".formatted(id)
                ));
        var furniture = furnitureRepository.findByVendorCode(vendorCode).get();
        furniture.setAvailability(Availability.INSTOCK);
        furnitureRepository.save(furniture);
        warehouse.addFurniture(furniture, amount);
        warehouseRepository.save(warehouse);
        return warehouse;
    }

    @Override
    public Optional<Warehouse> getWarehouseWithFurniture(Furniture furniture, Integer amount) {
        return warehouseRepository.findAll().stream()
                .filter((temp) -> temp.getStorage().containsKey(furniture)
                        &&
                        temp.getStorage().get(furniture) >= amount)
                .findFirst();
    }
}
