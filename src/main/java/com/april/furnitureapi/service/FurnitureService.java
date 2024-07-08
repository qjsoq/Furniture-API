package com.april.furnitureapi.service;

import com.april.furnitureapi.domain.Furniture;

import java.util.*;

public interface FurnitureService {
    Furniture saveFurniture(Furniture furniture, String email);
    List<Furniture> findAll();
    Furniture findByVendorCode(String vendorCode);
}
