package com.april.furnitureapi.service;

import com.april.furnitureapi.domain.Warehouse;
import jakarta.persistence.criteria.CriteriaBuilder;

import java.util.List;

public interface WarehouseService {
    Warehouse save(Warehouse warehouse);
    List<Warehouse> getAll();
    Warehouse addFurniture(Long id, String vendorCode, Integer amount);
}
