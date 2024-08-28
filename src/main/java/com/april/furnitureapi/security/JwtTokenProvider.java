package com.april.furnitureapi.security;

import com.april.furnitureapi.domain.User;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@NoArgsConstructor
public class JwtTokenProvider {
    @Value("${JWT_SECRET}")
    String jwtSecret;
    @Value("${JWT_ISSUER}")
    String jwtIssuer;

    public String generateToken(User user) {
        return JWT.create()
                .withIssuer(jwtIssuer)
                .withSubject(user.getEmail())
                .withClaim("lastname", user.getLastname())
                .withExpiresAt(setTime())
                .sign(Algorithm.HMAC512(jwtSecret));
    }

    public Optional<DecodedJWT> decodedJwt(String token) throws JWTVerificationException {
        return Optional.of(JWT.require(Algorithm.HMAC512(jwtSecret))
                .withIssuer(jwtIssuer)
                .build()
                .verify(token));
    }

    public String getEmailFromToken(String token) {
        return JWT.require(Algorithm.HMAC512(jwtSecret))
                .withIssuer(jwtIssuer)
                .build()
                .verify(token)
                .getSubject();
    }

    private Instant setTime() {
        LocalDateTime dateTime = LocalDateTime.now();
        Instant instant = dateTime.atZone(ZoneId.of("Europe/Paris")).toInstant();
        return instant.plus(7, ChronoUnit.DAYS);
    }

}
