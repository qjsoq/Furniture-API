package com.april.furnitureapi.service.impl;

import com.april.furnitureapi.data.UserRepository;
import com.april.furnitureapi.domain.User;
import com.april.furnitureapi.exception.UserAlreadyExistsException;
import com.april.furnitureapi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    @Override
    @Transactional
    public User signUp(User user) {
        if(userRepository.existsByEmailOrUsername(user.getEmail(), user.getUsername())){
            throw new UserAlreadyExistsException("User with provided email or username already exists");
        }
        user.setPassword(encoder.encode(user.getPassword()));
        return userRepository.save(user);
    }
}
