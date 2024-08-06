package com.april.furnitureapi.web.dto.cart;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class CartDto {
    Long price;
    String cartCode;
    LocalDateTime createdAt;

}
