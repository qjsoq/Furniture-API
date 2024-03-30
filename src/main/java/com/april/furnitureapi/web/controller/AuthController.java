package com.april.furnitureapi.web.controller;

import com.april.furnitureapi.domain.User;
import com.april.furnitureapi.service.UserService;
import static com.april.furnitureapi.web.WebConstants.AUTH;
import static com.april.furnitureapi.web.WebConstants.SIGN_UP;

import com.april.furnitureapi.web.dto.user.UserCreationDto;
import com.april.furnitureapi.web.dto.user.UserDto;
import com.april.furnitureapi.web.mapper.Usermapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.mapstruct.control.MappingControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(path = AUTH, produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final Usermapper usermapper;

    @PostMapping(SIGN_UP)
    public ResponseEntity<UserDto> signUp(@Valid @RequestBody UserCreationDto userCreationDto){
        var newUser = userService.signUp(usermapper.toEntity(userCreationDto));
        return new ResponseEntity<>(usermapper.toPayload(newUser), HttpStatus.CREATED);
    }
}
