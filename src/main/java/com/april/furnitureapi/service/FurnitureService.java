package com.april.furnitureapi.service;

import com.april.furnitureapi.domain.Comment;
import com.april.furnitureapi.domain.Furniture;
import com.april.furnitureapi.domain.FurnitureCategory;
import com.april.furnitureapi.domain.FurnitureDomain;
import java.util.List;
import java.util.Optional;

public interface FurnitureService {
    Furniture saveFurniture(Furniture furniture, String email, Integer amount, Long warehouseId);

    List<Furniture> findAll();

    Furniture findByVendorCode(String vendorCode);

    List<Furniture> findByCategory(FurnitureCategory category);

    List<Furniture> findByDomainAndCategory(FurnitureCategory category, FurnitureDomain domain,
                                            Optional<String> sortBy);

    List<Comment> getComments(String vendorCode);

    Furniture update(Furniture furniture);

    void deleteFurniture(String vendorCode);

}
