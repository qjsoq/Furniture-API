package com.april.furnitureapi.service.impl;

import com.april.furnitureapi.data.CartRepository;
import com.april.furnitureapi.data.FurnitureRepository;
import com.april.furnitureapi.data.UserRepository;
import com.april.furnitureapi.data.WarehouseRepository;
import com.april.furnitureapi.domain.Availability;
import com.april.furnitureapi.domain.Cart;
import com.april.furnitureapi.domain.Furniture;
import com.april.furnitureapi.exception.FurnitureNotFoundException;
import com.april.furnitureapi.exception.UserNotFoundException;
import com.april.furnitureapi.service.CartService;
import com.april.furnitureapi.service.WarehouseService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final UserRepository userRepository;
    private final FurnitureRepository furnitureRepository;
    private final WarehouseRepository warehouseRepository;
    private final WarehouseService warehouseService;
    private final ObjectMapper objectMapper;
    private final CartRepository cartRepository;

    @Override
    public Cart addAndCreateCart(String vendorCode, String email) {
        var user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException(
                "User with provided email %s does not exist".formatted(email)
        ));
        var furniture = furnitureRepository.findByVendorCode(vendorCode)
                .orElseThrow(() -> new FurnitureNotFoundException(
                        "Furniture with provided vendor code %s does not exist".formatted(
                                vendorCode)
                ));
        return Cart.builder().creator(user).items(Map.of(furniture, 1)).price(furniture.getPrice())
                .cartCode(String.valueOf(new Random().nextInt(9999999 - 1000000 + 1) + 1000000))
                .build();
    }


    @Override
    public Cart addToCart(Cart cart, String vendorCode) {
        var furniture = furnitureRepository.findByVendorCode(vendorCode)
                .orElseThrow(() -> new FurnitureNotFoundException(
                        "Furniture with provided vendor code %s does not exist".formatted(
                                vendorCode)
                ));
        if (cart.getItems() == null) {
            cart.setItems(new HashMap<>());
        }
        warehouseService.getWarehouseWithFurniture(furniture,
                        (cart.getItems().get(furniture) != null ? cart.getItems().get(furniture) + 1 : 1))
                .orElseThrow(() -> new FurnitureNotFoundException(
                        "We dont have the required amount of furniture items %s in our warehouses".formatted(
                                furniture.getVendorCode())
                ));
        cart.getItems().merge(furniture, 1, Integer::sum);
        cart.setPrice(cart.getPrice() + furniture.getPrice());
        return cart;
    }

    @Override
    public Cart deleteFromCart(Cart cart, String vendorCode) {
        var furniture = furnitureRepository.findByVendorCode(vendorCode)
                .orElseThrow(() -> new FurnitureNotFoundException(
                        "Furniture with provided vendor code %s does not exist".formatted(
                                vendorCode)
                ));
        cart.setPrice(
                cart.getItems().containsKey(furniture) ? cart.getPrice() - furniture.getPrice() :
                        cart.getPrice());
        cart.getItems().computeIfPresent(furniture, (key, value) -> value == 1 ? null : value - 1);
        return cart;
    }

    @Override
    public Cart decodeCartCookie(String encodedCart) throws JsonProcessingException {
        byte[] decodedBytes = Base64.getUrlDecoder().decode(encodedCart);
        String cartJson = new String(decodedBytes);
        System.out.println(cartJson);
        return objectMapper.readValue(cartJson, Cart.class);
    }

    @Override
    @PreAuthorize("@cartChecker.checkIfTheCartIsEmpty(#cart)")
    public Cart checkout(Cart cart) {
        cart.getItems().entrySet()
                .forEach(this::updateFurnitureAvailability);
        return cartRepository.save(cart);
    }

    @Override
    public void deleteCart(String cartCode) {
        cartRepository.deleteByCartCode(cartCode);
    }

    private void updateFurnitureAvailability(Map.Entry<Furniture, Integer> temp) {
        var furniture = temp.getKey();
        var warehouse = warehouseService.getWarehouseWithFurniture(temp.getKey(), temp.getValue())
                .orElseThrow(() -> new FurnitureNotFoundException(
                        "This item %s has already been sold".formatted(furniture.getVendorCode())
                ));
        warehouse.getStorage().computeIfPresent(furniture, (key, value) -> value - temp.getValue());
        if (warehouse.getStorage().get(furniture) == 0) {
            furniture.setAvailability(Availability.OUTSTOCK);
            furnitureRepository.save(furniture);
        }
        warehouseRepository.save(warehouse);
    }
}
