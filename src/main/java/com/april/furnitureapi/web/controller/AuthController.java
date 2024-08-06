package com.april.furnitureapi.web.controller;

import static com.april.furnitureapi.web.WebConstants.API;
import static com.april.furnitureapi.web.WebConstants.AUTH;
import static com.april.furnitureapi.web.WebConstants.SIGN_IN;
import static com.april.furnitureapi.web.WebConstants.SIGN_UP;

import com.april.furnitureapi.service.UserService;
import com.april.furnitureapi.web.dto.auth.AuthenticationRequest;
import com.april.furnitureapi.web.dto.auth.AuthenticationResponse;
import com.april.furnitureapi.web.dto.user.UserCreationDto;
import com.april.furnitureapi.web.dto.user.UserDto;
import com.april.furnitureapi.web.dto.verification.VerificationDto;
import com.april.furnitureapi.web.mapper.AuthenicationMapper;
import com.april.furnitureapi.web.mapper.Usermapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(path = API + AUTH, produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final Usermapper usermapper;
    private final AuthenicationMapper authenicationMapper;
    private final JavaMailSender javaMailSender;

    @PostMapping(SIGN_UP)
    public ResponseEntity<UserDto> signUp(@Valid @RequestBody UserCreationDto userCreationDto) {
        var newUser = userService.signUp(usermapper.toEntity(userCreationDto));
        return new ResponseEntity<>(usermapper.toPayload(newUser), HttpStatus.CREATED);
    }

    @PostMapping(SIGN_IN)
    public ResponseEntity<AuthenticationResponse> signIn(
            @Valid @RequestBody AuthenticationRequest request) {
        System.out.println(javaMailSender.toString());
        return ResponseEntity.of(userService
                .signIn(request.getEmail(), request.getPassword())
                .map(authenicationMapper::toAuthResponse));
    }

    @GetMapping("/")
    public ResponseEntity<VerificationDto> verifyToken(@RequestParam("token") String token) {
        boolean isVerified =
                userService.verifyToken(token);  // Assuming verifyToken returns a boolean
        return ResponseEntity.ok().body(VerificationDto.builder()
                .message("Your account was successfully verified!")
                .isVerified(isVerified)
                .build()
        );
    }
}
