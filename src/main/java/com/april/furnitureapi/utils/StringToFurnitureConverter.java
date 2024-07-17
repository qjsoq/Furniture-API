package com.april.furnitureapi.utils;

import com.april.furnitureapi.domain.Furniture;
import com.april.furnitureapi.domain.FurnitureCategory;
import com.april.furnitureapi.exception.InvalidCategoryValueException;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToFurnitureConverter implements Converter<String, FurnitureCategory> {
    @Override
    public FurnitureCategory convert(String source) {
        try {
            return FurnitureCategory.valueOf(source.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidCategoryValueException("Invalid category: " + source);
        }
    }
}
