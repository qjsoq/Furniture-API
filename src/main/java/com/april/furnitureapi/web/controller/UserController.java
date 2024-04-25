package com.april.furnitureapi.web.controller;

import com.april.furnitureapi.service.UserService;
import com.april.furnitureapi.web.dto.user.UserDto;
import com.april.furnitureapi.web.dto.user.UserUpdateDto;
import com.april.furnitureapi.web.mapper.Usermapper;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

import static com.april.furnitureapi.web.WebConstants.API;

@RestController
@RequestMapping(path = API + "/users",  produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class UserController {
    UserService userService;
    Usermapper usermapper;

    @GetMapping("/self")
    public ResponseEntity<UserDto> getSelf(Principal principal){
        var user = userService.findByEmail(principal.getName());
        return ResponseEntity.ok().body(usermapper.toPayload(user));
    }

    @PatchMapping("/self")
    public ResponseEntity<UserDto> updateSelf(Principal principal, @RequestBody @Valid UserUpdateDto updateDto){
        var user = userService.findByEmail(principal.getName());
        System.out.println(user.getEmail());
        var updatedUser = userService.updateUser(usermapper.partialUpdate(updateDto, user));
        return ResponseEntity.ok(usermapper.toPayload(updatedUser));
    }

}
