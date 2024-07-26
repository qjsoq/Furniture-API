package com.april.furnitureapi.web.controller;

import com.april.furnitureapi.domain.Cart;
import com.april.furnitureapi.service.CartService;
import com.april.furnitureapi.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Base64;

import static com.april.furnitureapi.web.WebConstants.API;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = API + "/cart", produces = MediaType.APPLICATION_JSON_VALUE)
public class CartController {
    private final CartService cartService;
    private final UserService userService;
    private final ObjectMapper objectMapper;

    @PostMapping("/add/{vendorCode}")
    public ResponseEntity<Cart> addToCart(@PathVariable String vendorCode, Principal principal,
                                          HttpServletResponse response, HttpServletRequest request) throws JsonProcessingException {
        Cart newCart = null;
        String cookieName = "cart_" + principal.getName().replaceAll("@", "_");
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookieName.equals(cookie.getName())) {
                    String encodedCart = cookie.getValue();
                    newCart = decodeCartCookie(encodedCart);
                    newCart = cartService.addToCart(newCart, vendorCode);
                    break;
                }
            }
        }
        if (newCart == null) {
            newCart = cartService.addAndCreateCart(vendorCode, principal.getName());
        }
        String cartJson = objectMapper.writeValueAsString(newCart);
        String encodedUpdatedCart = Base64.getUrlEncoder().encodeToString(cartJson.getBytes());

        Cookie newCartCookie = new Cookie(cookieName, encodedUpdatedCart);
        newCartCookie.setHttpOnly(true);
        newCartCookie.setPath("/");
        newCartCookie.setMaxAge(1000);
        response.addCookie(newCartCookie);

        return ResponseEntity.ok(newCart);
    }

    private Cart decodeCartCookie(String encodedCart) throws JsonProcessingException {
        byte[] decodedBytes = Base64.getUrlDecoder().decode(encodedCart);
        String cartJson = new String(decodedBytes);
        System.out.println(cartJson);
        return objectMapper.readValue(cartJson, Cart.class);
    }
}
