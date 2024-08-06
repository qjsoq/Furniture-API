package com.april.furnitureapi.utils;

import com.april.furnitureapi.domain.FurnitureDomain;
import com.april.furnitureapi.exception.InvalidDomainValueException;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToFurnitureDomainConverter implements Converter<String, FurnitureDomain> {
    @Override
    public FurnitureDomain convert(String source) {
        try {
            return FurnitureDomain.valueOf(source.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidDomainValueException("Invalid domain: " + source);
        }
    }
}
