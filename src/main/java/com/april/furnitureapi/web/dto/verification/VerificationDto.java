package com.april.furnitureapi.web.dto.verification;

import lombok.experimental.SuperBuilder;

@SuperBuilder
public class VerificationDto {
    String message;
    boolean isVerified;
}
