package com.april.furnitureapi.web.controller;

import com.april.furnitureapi.service.UserService;
import com.april.furnitureapi.web.dto.user.UserDto;
import com.april.furnitureapi.web.mapper.Usermapper;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping(path = "/users",  produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class UserController {
    UserService userService;
    Usermapper usermapper;

    @GetMapping("/self")
    public ResponseEntity<UserDto> getSelf(Principal principal){
        System.out.println(principal.getName());
        var user = userService.findByEmail(principal.getName());
        return ResponseEntity.ok().body(usermapper.toPayload(user));
    }

}
