package com.april.furnitureapi.web.dto.auth;

import java.util.Date;
import lombok.Data;

@Data
public class AuthenticationResponse {
    private String token;
    private String type;
    private String algorithm;
    private Date expires;
}
