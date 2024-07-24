package com.april.furnitureapi.data;

import com.april.furnitureapi.domain.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
}
