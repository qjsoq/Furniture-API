package com.april.furnitureapi.security;

import com.april.furnitureapi.domain.User;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
    @Value(value = SecurityConstants.SECRET_KEY)
    String jwtSecret;
    @Value(value = "furniture-api")
    String jwtIssuer;
    public String generateToken(User user){
        return JWT.create()
                .withIssuer(jwtIssuer)
                .withSubject(user.getEmail())
                .withClaim("lastname", user.getLastname())
                .withExpiresAt(LocalDate.now()
                        .plusDays(15)
                        .atStartOfDay(ZoneId.systemDefault())
                        .toInstant())
                .sign(Algorithm.HMAC512(jwtSecret));
    }

    public Optional<DecodedJWT> decodedJwt(String token){
        try {
            return Optional.of(JWT.require(Algorithm.HMAC512(jwtSecret))
                    .withIssuer(jwtIssuer)
                    .build()
                    .verify(token));
        } catch (JWTVerificationException exception) {
            return Optional.empty();
        }
    }
}
