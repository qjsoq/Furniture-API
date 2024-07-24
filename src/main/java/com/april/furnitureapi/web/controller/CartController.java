package com.april.furnitureapi.web.controller;

import com.april.furnitureapi.domain.Cart;
import com.april.furnitureapi.service.CartService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Base64;

import static com.april.furnitureapi.web.WebConstants.API;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = API + "/cart", produces = MediaType.APPLICATION_JSON_VALUE)
public class CartController {
    private final CartService cartService;
    private final ObjectMapper objectMapper;

    @PostMapping("/add/{vendorCode}")
    public ResponseEntity<Void> addToCart(@PathVariable String vendorCode, Principal principal,
                                          HttpServletResponse response, HttpServletRequest request) throws JsonProcessingException {
        Cart newCart;
        newCart = cartService.addAndCreateCart(vendorCode, principal.getName());
        String cartJson = objectMapper.writeValueAsString(newCart);
        String encodedCart = Base64.getUrlEncoder().encodeToString(cartJson.getBytes());
        Cookie cookie = new Cookie("cart_" + principal.getName().replaceAll("@", "_"), encodedCart);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(1000);
        response.addCookie(cookie);
        return ResponseEntity.ok().build();
    }
}
