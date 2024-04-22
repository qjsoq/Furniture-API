package com.april.furnitureapi.web.dto.auth;

import lombok.Data;

import java.util.Date;

@Data
public class AuthenticationResponse {
    private String token;
    private String type;
    private String algorithm;
    private Date expires;
}
