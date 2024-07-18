package com.april.furnitureapi.data;

import com.april.furnitureapi.domain.Furniture;
import com.april.furnitureapi.domain.FurnitureCategory;
import com.april.furnitureapi.domain.FurnitureDomain;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FurnitureRepository extends JpaRepository<Furniture, Long> {
    boolean existsByVendorCode(String vendorCode);
    Furniture findByVendorCode(String vendorCode);
    List<Furniture> findByCategory(FurnitureCategory category);
    List<Furniture> findByDomainAndCategory(FurnitureDomain domain, FurnitureCategory category, Sort sort);

}
