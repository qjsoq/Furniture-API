package com.april.furnitureapi.data;

import com.april.furnitureapi.domain.Furniture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FurnitureRepository extends JpaRepository<Furniture, Long> {
    boolean existsByVendorCode(String vendorCode);
    Furniture findByVendorCode(String vendorCode);
}
