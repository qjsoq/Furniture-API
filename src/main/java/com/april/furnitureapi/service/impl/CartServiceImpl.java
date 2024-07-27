package com.april.furnitureapi.service.impl;

import com.april.furnitureapi.data.CartRepository;
import com.april.furnitureapi.data.FurnitureRepository;
import com.april.furnitureapi.data.UserRepository;
import com.april.furnitureapi.domain.Cart;
import com.april.furnitureapi.exception.FurnitureNotFoundException;
import com.april.furnitureapi.exception.UserNotFoundException;
import com.april.furnitureapi.service.CartService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final UserRepository userRepository;
    private final FurnitureRepository furnitureRepository;
    private final ObjectMapper objectMapper;
    private final CartRepository cartRepository;

    @Override
    public Cart addAndCreateCart(String vendorCode, String email) {
        var user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException(
                "User with provided email %s does not exist".formatted(email)
        ));
        var furniture = furnitureRepository.findByVendorCode(vendorCode).orElseThrow(() -> new FurnitureNotFoundException(
                "Furniture with provided vendor code %s does not exist".formatted(vendorCode)
        ));
        return Cart.builder().creator(user).items(Map.of(furniture, 1)).price(furniture.getPrice()).build();
    }

    @Override
    public Cart addToCart(Cart cart, String vendorCode) {
        var furniture = furnitureRepository.findByVendorCode(vendorCode).orElseThrow(() -> new FurnitureNotFoundException(
                "Furniture with provided vendor code %s does not exist".formatted(vendorCode)
        ));
        if (cart.getItems() == null) {
            cart.setItems(new HashMap<>());
        }
        cart.getItems().merge(furniture, 1, Integer::sum);
        cart.setPrice(cart.getPrice() + furniture.getPrice());
        return cart;
    }

    @Override
    public Cart deleteFromCart(Cart cart, String vendorCode) {
        var furniture = furnitureRepository.findByVendorCode(vendorCode).orElseThrow(() -> new FurnitureNotFoundException(
                "Furniture with provided vendor code %s does not exist".formatted(vendorCode)
        ));
        cart.setPrice(cart.getItems().containsKey(furniture)? cart.getPrice() - furniture.getPrice() : cart.getPrice());
        cart.getItems().computeIfPresent(furniture, (key, value) -> value == 1 ? null : value - 1);
        return cart;
    }

    @Override
    public Cart decodeCartCookie(String encodedCart) throws JsonProcessingException {
        byte[] decodedBytes = Base64.getUrlDecoder().decode(encodedCart);
        String cartJson = new String(decodedBytes);
        System.out.println(cartJson);
        return objectMapper.readValue(cartJson, Cart.class);    }

    @Override
    public Cart checkout(Cart cart) {
        return cartRepository.save(cart);
    }
}
