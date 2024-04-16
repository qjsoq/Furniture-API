package com.april.furnitureapi.service.impl;

import com.april.furnitureapi.data.UserRepository;
import com.april.furnitureapi.domain.User;
import com.april.furnitureapi.exception.InvalidPasswordException;
import com.april.furnitureapi.exception.UserAlreadyExistsException;
import com.april.furnitureapi.exception.UserNotFoundException;
import com.april.furnitureapi.security.JwtTokenProvider;
import com.april.furnitureapi.service.UserService;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final JwtTokenProvider jwtTokenProvider;
    @Override
    @Transactional
    public User signUp(User user) {
        if(userRepository.existsByEmailOrUsername(user.getEmail(), user.getUsername())){
            throw new UserAlreadyExistsException("User with provided email or username already exists");
        }
        user.setPassword(encoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public Optional<DecodedJWT> signIn(String email, String password) {
        var user = userRepository.findByEmail(email).orElseThrow(() ->new UserNotFoundException(
                "User with email %s doesn`t exist".formatted(email)
        ));
        if(!encoder.matches(password, user.getPassword())){
            throw new InvalidPasswordException("Invalid password");
        }
        var token = jwtTokenProvider.generateToken(user);
        return jwtTokenProvider.decodedJwt(token);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(String.format("User with provided email %s doesn't exist", email)));
    }


}
