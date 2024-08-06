package com.april.furnitureapi.web.dto.cart;

import com.april.furnitureapi.web.dto.furniture.FurnitureDto;
import java.time.LocalDateTime;
import java.util.Map;
import lombok.Data;

@Data
public class CartDetailedDto {
    Long price;
    String cartCode;
    LocalDateTime createdAt;
    Map<FurnitureDto, Integer> items;
}
