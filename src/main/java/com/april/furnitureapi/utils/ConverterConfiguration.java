package com.april.furnitureapi.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//@Configuration
@RequiredArgsConstructor
public class ConverterConfiguration implements WebMvcConfigurer {
    private final StringToFurnitureCategoryConverter stringToFurnitureCategoryConverter;
    private final StringToFurnitureDomainConverter domainConverter;

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(stringToFurnitureCategoryConverter);
        registry.addConverter(domainConverter);
    }

}
