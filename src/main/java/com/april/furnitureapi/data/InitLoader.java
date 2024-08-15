package com.april.furnitureapi.data;

import com.april.furnitureapi.domain.Address;
import com.april.furnitureapi.domain.Role;
import com.april.furnitureapi.domain.User;
import com.april.furnitureapi.domain.Warehouse;
import java.util.HashMap;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InitLoader implements CommandLineRunner {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;
    private final WarehouseRepository warehouseRepository;

    @Override
    public void run(String... args) throws Exception {
        var roleUser = roleRepository.findByName("ROLE_USER")
                .orElseGet(() -> roleRepository.save(Role.builder().name("ROLE_USER").build()));
        var roleAdmin = roleRepository.findByName("ROLE_ADMIN")
                .orElseGet(() -> roleRepository.save(Role.builder().name("ROLE_ADMIN").build()));
        if (warehouseRepository.findByName("Dublin").isEmpty()) {
            warehouseRepository.save(Warehouse.builder().name("Dublin").address(
                    Address.FONTENOY_STR).storage(new HashMap<>()).build());
        }
        if (userRepository.findByUsername("admin").isEmpty()) {
            var admin = new User();
            admin.setName("Admin");
            admin.setUsername("admin");
            admin.setEmail("admin@gmail.com");
            admin.setPassword(encoder.encode("123456"));
            admin.setRoles(Set.of(roleUser, roleAdmin));
            admin.setVerified(true);
            userRepository.save(admin);
        }
    }
}
