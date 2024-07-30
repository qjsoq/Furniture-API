package com.april.furnitureapi.web.dto.warehouse;

import com.april.furnitureapi.web.dto.furniture.FurnitureDto;
import lombok.Data;

import java.util.Map;

@Data
public class WarehouseDetailedDto {
    String name;
    Map<FurnitureDto, Integer> storage;
}
