package com.april.furnitureapi.web.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserCreationDto {
    @NotBlank(message = "Name cannot be empty")
    @Pattern(regexp = "^[a-zA-Z]+$", message = "Only latin letters are allowed")
    @Size(min = 2, max = 32, message = "Name must contain at least 2 characters, and no more than 32 characters")
    String name;
    @NotBlank(message = "Last name cannot be empty")
    @Pattern(regexp = "^[a-zA-Z]+$", message = "Only latin letters are allowed")
    @Size(min = 2, max = 32, message = "Last name must contain at least 2 characters, and no more than 32 characters")
    String lastname;
    @NotBlank(message = "Username cannot be empty")
    @Pattern(regexp = "^\\w+$", message = "You can use a-z, 0-9 and underscores")
    @Size(min = 3, max = 32, message = "Username must contain at least 3 characters, and no more than 32 characters")
    String username;
    @NotBlank(message = "Specify password")
    @Size(min = 5, max = 32, message = "Enter at least 6 and less than 32 characters")
    String password;
    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Provide valid email")
    String email;
}
