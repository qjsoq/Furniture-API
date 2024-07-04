package com.april.furnitureapi.web.dto.furniture;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FurnitureDto {
    String title;
    Long price;
}
