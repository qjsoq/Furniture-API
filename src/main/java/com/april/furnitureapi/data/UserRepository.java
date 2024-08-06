package com.april.furnitureapi.data;

import com.april.furnitureapi.domain.User;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    boolean existsByEmailOrUsername(String email, String username);

    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);
}
