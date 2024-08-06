package com.april.furnitureapi.web.controller;

import static com.april.furnitureapi.web.WebConstants.API;

import com.april.furnitureapi.domain.Cart;
import com.april.furnitureapi.exception.CartNotFoundException;
import com.april.furnitureapi.service.CartService;
import com.april.furnitureapi.service.CookieService;
import com.april.furnitureapi.web.dto.cart.CartDetailedDto;
import com.april.furnitureapi.web.mapper.CartMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.net.URI;
import java.security.Principal;
import java.util.HashMap;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = API + "/cart", produces = MediaType.APPLICATION_JSON_VALUE)
public class CartController {
    private final CartService cartService;
    private final CookieService cookieService;
    private final CartMapper cartMapper;

    @PostMapping("/checkout")
    public ResponseEntity<CartDetailedDto> saveCart(Principal principal, HttpServletRequest request,
                                                    HttpServletResponse response)
            throws JsonProcessingException {
        Cart newCart;
        var cookie = cookieService.extractCookie(principal.getName(), request);
        if (cookie.isPresent()) {
            String encodedCart = cookie.get().getValue();
            newCart = cartService.decodeCartCookie(encodedCart);
            newCart = cartService.checkout(newCart);
        } else {
            throw new CartNotFoundException("You have not add anything to the cart");
        }
        newCart.setPrice(0L);
        newCart.setItems(new HashMap<>());
        response.addCookie(cookieService.getNewCookie(newCart, principal.getName(), 1));
        return ResponseEntity.created(URI.create("")).body(cartMapper.toDetailedDto(newCart));
    }

    @PutMapping("/add/{vendorCode}")
    @PreAuthorize("@furnitureChecker.checkAvailability(#vendorCode)")
    public ResponseEntity<CartDetailedDto> addToCart(@PathVariable String vendorCode,
                                                     Principal principal,
                                                     HttpServletResponse response,
                                                     HttpServletRequest request)
            throws JsonProcessingException {
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
        return ResponseEntity.ok(cartMapper.toDetailedDto(newCart));
    }

    @DeleteMapping("/delete/{vendorCode}")
    public ResponseEntity<Cart> deleteFromCart(@PathVariable String vendorCode, Principal principal,
                                               HttpServletResponse response,
                                               HttpServletRequest request)
            throws JsonProcessingException {
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

    @DeleteMapping("/{cartCode}")
    @Transactional
    @PreAuthorize("@cartChecker.isUserTheAuthor(#cartCode, #principal.name)")
    public ResponseEntity<Void> deleteCart(@PathVariable String cartCode, Principal principal) {
        cartService.deleteCart(cartCode);
        return ResponseEntity.noContent().build();
    }
}
