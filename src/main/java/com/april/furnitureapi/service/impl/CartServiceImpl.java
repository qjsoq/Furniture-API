package com.april.furnitureapi.service.impl;

import com.april.furnitureapi.data.FurnitureRepository;
import com.april.furnitureapi.data.UserRepository;
import com.april.furnitureapi.domain.Cart;
import com.april.furnitureapi.exception.FurnitureNotFoundException;
import com.april.furnitureapi.exception.UserNotFoundException;
import com.april.furnitureapi.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final UserRepository userRepository;
    private final FurnitureRepository furnitureRepository;

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
        System.out.println(cart.getItems().get(furniture));
        return cart;
    }
}
