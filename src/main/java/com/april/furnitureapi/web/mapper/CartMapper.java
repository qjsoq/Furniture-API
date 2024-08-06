package com.april.furnitureapi.web.mapper;

import com.april.furnitureapi.domain.Cart;
import com.april.furnitureapi.web.dto.cart.CartDetailedDto;
import com.april.furnitureapi.web.dto.cart.CartDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CartMapper {
    CartDto toDto(Cart cart);

    CartDetailedDto toDetailedDto(Cart cart);
}
