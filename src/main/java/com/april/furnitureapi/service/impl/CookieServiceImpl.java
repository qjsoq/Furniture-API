package com.april.furnitureapi.service.impl;

import com.april.furnitureapi.domain.Cart;
import com.april.furnitureapi.service.CookieService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Optional;
@Service
@RequiredArgsConstructor
public class CookieServiceImpl implements CookieService {
    private final ObjectMapper objectMapper;
    @Override
    public Optional<Cookie> extractCookie(String email, HttpServletRequest request) {
        String cookieName = getCookieName(email);
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookieName.equals(cookie.getName())) {
                    return Optional.of(cookie);
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public Cookie getNewCookie(Cart cart, String email, Integer timeout) throws JsonProcessingException {
        String cartJson = objectMapper.writeValueAsString(cart);
        String encodedUpdatedCart = Base64.getUrlEncoder().encodeToString(cartJson.getBytes());

        Cookie newCartCookie = new Cookie(getCookieName(email), encodedUpdatedCart);
        newCartCookie.setHttpOnly(true);
        newCartCookie.setPath("/");
        newCartCookie.setMaxAge(timeout);
        return newCartCookie;
    }

    @Override
    public String getCookieName(String email) {
        return "cart_" + email.replaceAll("@", "_");
    }

}
