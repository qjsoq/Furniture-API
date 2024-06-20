package com.april.furnitureapi.web.dto.verification;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Data
public class VerificationDto {
    String message;
    boolean isVerified;
}
