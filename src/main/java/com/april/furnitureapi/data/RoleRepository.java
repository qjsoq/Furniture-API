package com.april.furnitureapi.data;

import com.april.furnitureapi.domain.Role;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, UUID> {
    Role findByName(String name);
}
