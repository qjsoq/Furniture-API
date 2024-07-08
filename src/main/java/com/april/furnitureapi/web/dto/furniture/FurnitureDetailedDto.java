package com.april.furnitureapi.web.dto.furniture;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FurnitureDetailedDto {
    String title;
    Long price;
    String description;
    String rating;
    String vendorCode;
}
