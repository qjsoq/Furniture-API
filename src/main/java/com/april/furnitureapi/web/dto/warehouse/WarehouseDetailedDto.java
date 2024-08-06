package com.april.furnitureapi.web.dto.warehouse;

import com.april.furnitureapi.web.dto.furniture.FurnitureDto;
import java.util.Map;
import lombok.Data;

@Data
public class WarehouseDetailedDto {
    String name;
    Map<FurnitureDto, Integer> storage;
}
