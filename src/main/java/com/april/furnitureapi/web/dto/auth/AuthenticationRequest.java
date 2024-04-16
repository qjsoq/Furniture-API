package com.april.furnitureapi.web.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AuthenticationRequest {
    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Provide valid email")
    String email;
    @NotBlank(message = "Specify password")
    @Size(min = 6, max = 32, message = "Enter at least 6 and less than 32 characters")
    String password;
}
