package com.april.furnitureapi.service;

import com.april.furnitureapi.domain.Cart;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface CartService {
    Cart addAndCreateCart(String vendorCode, String email);

    Cart addToCart(Cart cart, String vendorCode);

    Cart deleteFromCart(Cart cart, String vendorCode);

    Cart decodeCartCookie(String encodedCart) throws JsonProcessingException;

    Cart checkout(Cart cart);

    void deleteCart(String cartCode);
}
