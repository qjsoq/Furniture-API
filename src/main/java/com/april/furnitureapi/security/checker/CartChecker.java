package com.april.furnitureapi.security.checker;

import com.april.furnitureapi.data.CartRepository;
import com.april.furnitureapi.domain.Cart;
import com.april.furnitureapi.exception.CartNotFoundException;
import com.april.furnitureapi.exception.CommentNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CartChecker {
    private final CartRepository cartRepository;

    public boolean checkIfTheCartIsEmpty(Cart cart){
        if(cart.getItems().isEmpty()) throw new CartNotFoundException(
                "Your cart is empty"
        );
        return true;
    }
    public boolean isUserTheAuthor(String cartCode, String email){
        var cart = cartRepository.findByCartCode(cartCode).orElseThrow(() -> new CommentNotFoundException(
                "Cart not found"
        ));
        return cart.getCreator().getEmail().equals(email);
    }
}
