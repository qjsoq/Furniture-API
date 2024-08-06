package com.april.furnitureapi.data;

import com.april.furnitureapi.domain.Cart;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
    List<Cart> findByCreatorId(UUID id);

    Optional<Cart> findByCartCode(String cartCode);

    void deleteByCartCode(String cartCode);
}
