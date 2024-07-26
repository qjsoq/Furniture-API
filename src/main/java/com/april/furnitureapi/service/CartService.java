package com.april.furnitureapi.service;

import com.april.furnitureapi.domain.Cart;

public interface CartService {
    Cart addAndCreateCart(String vendorCode, String email);
    Cart addToCart(Cart cart, String vendorCode);
}
