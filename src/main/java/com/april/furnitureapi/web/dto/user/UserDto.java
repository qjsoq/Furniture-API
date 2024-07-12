package com.april.furnitureapi.web.dto.user;

import com.april.furnitureapi.domain.Role;
import lombok.Data;

import java.util.Collection;
import java.util.Set;
import java.util.UUID;

@Data
public class UserDto {
    private UUID id;
    private String username;
    private String name;
    private String lastname;
    private String email;
    private Collection<Role> roles;
}
