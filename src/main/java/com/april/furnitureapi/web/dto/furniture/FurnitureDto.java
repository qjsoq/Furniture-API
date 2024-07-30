package com.april.furnitureapi.web.dto.furniture;

import com.april.furnitureapi.domain.Availability;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FurnitureDto {
    String title;
    String vendorCode;
    Long price;
    Availability availability;
}
