package com.april.furnitureapi.data;

import com.april.furnitureapi.domain.Confirmation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ConfirmationRepository extends JpaRepository<Confirmation, UUID> {
    Confirmation findByToken(String token);
}
