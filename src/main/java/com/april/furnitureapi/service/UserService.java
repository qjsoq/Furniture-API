package com.april.furnitureapi.service;

import com.april.furnitureapi.domain.User;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Optional;

public interface UserService {
    User signUp(User user);
    Optional<DecodedJWT> signIn(String email, String password);
    User findByEmail(String email);
}
