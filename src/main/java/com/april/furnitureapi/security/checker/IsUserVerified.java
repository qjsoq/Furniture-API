package com.april.furnitureapi.security.checker;

import com.april.furnitureapi.data.UserRepository;
import com.april.furnitureapi.exception.UserIsNotVerifiedException;
import com.april.furnitureapi.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class IsUserVerified {
    private final UserRepository userRepository;

    public boolean isEmailVerified(String email) {
        var user = userRepository.findByEmail(email).orElseThrow(
                () -> new UserNotFoundException(
                        "User with this email %s was not found".formatted(email))
        );
        if (!user.isVerified()) {
            throw new UserIsNotVerifiedException("Please verify your account");
        }
        return true;
    }
}
