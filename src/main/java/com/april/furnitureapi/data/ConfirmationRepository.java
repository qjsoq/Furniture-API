package com.april.furnitureapi.data;

import com.april.furnitureapi.domain.Confirmation;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfirmationRepository extends JpaRepository<Confirmation, UUID> {
    Confirmation findByToken(String token);
}
