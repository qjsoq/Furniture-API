package com.april.furnitureapi.web.controller;

import com.april.furnitureapi.domain.Cart;
import com.april.furnitureapi.exception.CartNotFoundException;
import com.april.furnitureapi.service.CartService;
import com.april.furnitureapi.service.CookieService;
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

import java.net.URI;
import java.security.Principal;
import java.util.Base64;

import static com.april.furnitureapi.web.WebConstants.API;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = API + "/cart", produces = MediaType.APPLICATION_JSON_VALUE)
public class CartController {
    private final CartService cartService;
    private final CookieService cookieService;

    @PostMapping("/checkout")
    public ResponseEntity<Cart> saveCart(Principal principal, HttpServletRequest request, HttpServletResponse response) throws JsonProcessingException {
        Cart newCart;
        var cookie = cookieService.extractCookie(principal.getName(), request);
        if (cookie.isPresent()) {
            String encodedCart = cookie.get().getValue();
            newCart = cartService.decodeCartCookie(encodedCart);
            newCart = cartService.checkout(newCart);
        } else {
            throw new CartNotFoundException("You have not add anything to the cart");
        }
        response.addCookie(cookieService.getNewCookie(newCart, principal.getName(), 1));
        return ResponseEntity.created(URI.create("")).body(newCart);
    }

    @PutMapping("/add/{vendorCode}")
    public ResponseEntity<Cart> addToCart(@PathVariable String vendorCode, Principal principal,
                                          HttpServletResponse response, HttpServletRequest request) throws JsonProcessingException {
        Cart newCart;
        var cookie = cookieService.extractCookie(principal.getName(), request);
        if (cookie.isPresent()) {
            String encodedCart = cookie.get().getValue();
            newCart = cartService.decodeCartCookie(encodedCart);
            newCart = cartService.addToCart(newCart, vendorCode);
        } else {
            newCart = cartService.addAndCreateCart(vendorCode, principal.getName());
        }
        response.addCookie(cookieService.getNewCookie(newCart, principal.getName(), 1000));
        return ResponseEntity.ok(newCart);
    }

    @DeleteMapping("/delete/{vendorCode}")
    public ResponseEntity<Cart> deleteFromCart(@PathVariable String vendorCode, Principal principal,
                                               HttpServletResponse response, HttpServletRequest request) throws JsonProcessingException {
        Cart newCart;
        var cookie = cookieService.extractCookie(principal.getName(), request);
        if (cookie.isPresent()) {
            String encodedCart = cookie.get().getValue();
            newCart = cartService.decodeCartCookie(encodedCart);
            newCart = cartService.deleteFromCart(newCart, vendorCode);
        } else {
            throw new CartNotFoundException("You have not add anything to the cart");
        }
        response.addCookie(cookieService.getNewCookie(newCart, principal.getName(), 1000));
        return ResponseEntity.noContent().build();
    }


}
