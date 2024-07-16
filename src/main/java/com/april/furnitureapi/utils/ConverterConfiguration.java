package com.april.furnitureapi.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class ConverterConfiguration implements WebMvcConfigurer {
    private final StringToFurnitureConverter stringToFurnitureCategoryConverter;

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(stringToFurnitureCategoryConverter);
    }

}
