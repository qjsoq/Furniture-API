package com.april.furnitureapi.security.checker;

import com.april.furnitureapi.domain.Cart;
import com.april.furnitureapi.exception.CartNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CartChecker {

    public boolean checkIfTheCartIsEmpty(Cart cart){
        if(cart.getItems().isEmpty()) throw new CartNotFoundException(
                "Your cart is empty"
        );
        return true;
    }
}
