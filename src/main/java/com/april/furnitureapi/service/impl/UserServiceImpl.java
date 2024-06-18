package com.april.furnitureapi.service.impl;

import com.april.furnitureapi.data.ConfirmationRepository;
import com.april.furnitureapi.data.UserRepository;
import com.april.furnitureapi.domain.Confirmation;
import com.april.furnitureapi.domain.User;
import com.april.furnitureapi.exception.InvalidPasswordException;
import com.april.furnitureapi.exception.UserAlreadyExistsException;
import com.april.furnitureapi.exception.UserNotFoundException;
import com.april.furnitureapi.security.JwtTokenProvider;
import com.april.furnitureapi.service.EmailService;
import com.april.furnitureapi.service.UserService;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final EmailService emailService;
    private final UserRepository userRepository;
    private final ConfirmationRepository confirmationRepository;
    private final PasswordEncoder encoder;
    private final JwtTokenProvider jwtTokenProvider;
    @Override
    @Transactional
    public User signUp(User user) {
        checkIfUserExists(user);
        user.setPassword(encoder.encode(user.getPassword()));
        confirmationRepository.save(new Confirmation(user));
        emailService.sendVerificationEmail(user);
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
    @Override
    public User updateUser(User user){
        checkIfUserExists(user);
        user.setPassword(encoder.encode(user.getPassword()));
        return userRepository.save(user);
    }
    public void checkIfUserExists(User user){
        var email = user.getEmail();
        var username = user.getUsername();
        var id = user.getId();
        if(isEmailInUse(email, id)){
            throw new UserAlreadyExistsException("User with provided email %s exists".formatted(email));
        } else if (isUserNameInUse(username, id)) {
            throw new UserAlreadyExistsException("User with provided username %s exists".formatted(username));
        }
    }
    private boolean isEmailInUse(String email, UUID id){
        return userRepository.findByEmail(email).filter(foundedUser -> foundedUser.getId() != id).isPresent();
    }
    private boolean isUserNameInUse(String username, UUID id){
        return userRepository.findByUsername(username).filter(foundedUser -> foundedUser.getId() != id).isPresent();
    }


}
