package com.april.furnitureapi.web.dto.cart;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CartDto {
    Long price;
    String cartCode;
    LocalDateTime createdAt;

}
