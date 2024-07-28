package com.april.furnitureapi.data;

import com.april.furnitureapi.domain.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CartRepository extends JpaRepository<Cart, Long> {
    List<Cart> findByCreatorId(UUID id);
}
