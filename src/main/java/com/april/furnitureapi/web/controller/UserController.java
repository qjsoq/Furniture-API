package com.april.furnitureapi.web.controller;

import static com.april.furnitureapi.web.WebConstants.API;
import static com.april.furnitureapi.web.WebConstants.USERS;

import com.april.furnitureapi.service.UserService;
import com.april.furnitureapi.web.dto.cart.CartDto;
import com.april.furnitureapi.web.dto.user.UserDto;
import com.april.furnitureapi.web.dto.user.UserUpdateDto;
import com.april.furnitureapi.web.mapper.CartMapper;
import com.april.furnitureapi.web.mapper.Usermapper;
import jakarta.validation.Valid;
import java.security.Principal;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = API + USERS, produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class UserController {
    UserService userService;
    Usermapper usermapper;
    CartMapper cartMapper;

    @GetMapping("/self")
    public ResponseEntity<UserDto> getSelf(Principal principal) {
        var user = userService.findByEmail(principal.getName());
        return ResponseEntity.ok().body(usermapper.toPayload(user));
    }

    @GetMapping("/myOrders")
    public ResponseEntity<List<CartDto>> getMyOrders(Principal principal) {
        var orders = userService.getMyOrders(principal.getName());
        return ResponseEntity.ok(orders.stream()
                .map(cartMapper::toDto)
                .toList());
    }

    @PatchMapping("/self")
    public ResponseEntity<UserDto> updateSelf(Principal principal,
                                              @RequestBody @Valid UserUpdateDto updateDto) {
        var user = userService.findByEmail(principal.getName());
        var updatedUser = userService.updateUser(usermapper.partialUpdate(updateDto, user));
        return ResponseEntity.ok(usermapper.toPayload(updatedUser));
    }

}
