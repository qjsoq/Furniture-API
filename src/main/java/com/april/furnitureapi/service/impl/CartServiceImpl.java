package com.april.furnitureapi.service.impl;

import com.april.furnitureapi.data.FurnitureRepository;
import com.april.furnitureapi.data.UserRepository;
import com.april.furnitureapi.domain.Cart;
import com.april.furnitureapi.exception.FurnitureNotFoundException;
import com.april.furnitureapi.exception.UserNotFoundException;
import com.april.furnitureapi.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}
