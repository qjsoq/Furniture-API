package com.april.furnitureapi.service;

import com.april.furnitureapi.domain.Warehouse;

import java.util.List;

public interface WarehouseService {
    Warehouse save(Warehouse warehouse);
    List<Warehouse> getAll();
}
