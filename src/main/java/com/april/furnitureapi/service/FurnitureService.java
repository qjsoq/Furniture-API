package com.april.furnitureapi.service;

import com.april.furnitureapi.domain.Furniture;
import com.april.furnitureapi.domain.FurnitureCategory;
import com.april.furnitureapi.domain.FurnitureDomain;

import java.util.*;

public interface FurnitureService {
    Furniture saveFurniture(Furniture furniture, String email, Optional<String> availability);
    List<Furniture> findAll();
    Furniture findByVendorCode(String vendorCode);
    List<Furniture> findByCategory(FurnitureCategory category);
    List<Furniture> findByDomainAndCategory(FurnitureCategory category, FurnitureDomain domain, Optional<String> sortBy);

}
