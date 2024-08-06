package com.april.furnitureapi.web.dto.user;

import com.april.furnitureapi.domain.Role;
import java.util.Collection;
import java.util.UUID;
import lombok.Data;

@Data
public class UserDto {
    private UUID id;
    private String username;
    private String name;
    private String lastname;
    private String email;
    private Collection<Role> roles;
}
