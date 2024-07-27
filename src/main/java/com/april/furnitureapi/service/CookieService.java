package com.april.furnitureapi.service;

import com.april.furnitureapi.domain.Cart;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Optional;

public interface CookieService {
    Optional<Cookie> extractCookie(String email, HttpServletRequest request);
    Cookie getNewCookie(Cart cart, String email, Integer timeout) throws JsonProcessingException;
    String getCookieName(String email);
}
