package com.april.furnitureapi.web.dto.user;

import lombok.Data;

import java.util.UUID;

@Data
public class UserDto {
    private UUID id;
    private String username;
    private String name;
    private String lastname;
    private String email;
}
