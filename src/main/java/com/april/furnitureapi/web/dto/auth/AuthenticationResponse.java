package com.april.furnitureapi.web.dto.auth;

import lombok.Data;

@Data
public class AuthenticationResponse {
    private String token;
    private String type;
    private String algorithm;
    private Long expires;
}
