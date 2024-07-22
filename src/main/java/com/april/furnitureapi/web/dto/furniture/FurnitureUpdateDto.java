package com.april.furnitureapi.web.dto.furniture;

import com.april.furnitureapi.domain.Availability;
import lombok.Data;

@Data
public class FurnitureUpdateDto {
    Long price;
    String title;
    String description;
    String vendorCode;
    Availability availability;
}
