package com.april.furnitureapi.web.dto.furniture;

import com.april.furnitureapi.domain.FurnitureCategory;
import com.april.furnitureapi.domain.FurnitureDomain;
import com.april.furnitureapi.utils.PriceConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Range;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FurnitureCreationDto {
    @Range(min = 0, max = 1000000, message = "The length of price should be between 1 and 10 digits")
    @PriceConstraint(message = "Price can contain only digits")
    String price;
    @NotBlank(message = "Title cannot be blank")
    @Size(min = 3, max = 64, message = "Title of the furniture cannot be less than 3 and more than 64 characters ")
    String title;
    @NotBlank(message = "Description must be filled")
    @Size(min = 4, max = 2048, message = "Description of the furniture cannot be less than 4 and more than 2048 characters")
    String description;
    @Range(min = 0, max = 5)
    Double rating;
    FurnitureCategory category;
    FurnitureDomain domain;
}
