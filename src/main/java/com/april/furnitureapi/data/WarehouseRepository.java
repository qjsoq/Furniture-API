package com.april.furnitureapi.data;

import com.april.furnitureapi.domain.Warehouse;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {
    Optional<Warehouse> findByName(String name);
}
