package com.april.furnitureapi.web.mapper;

import com.april.furnitureapi.domain.Furniture;
import com.april.furnitureapi.web.dto.furniture.FurnitureCreationDto;
import com.april.furnitureapi.web.dto.furniture.FurnitureDetailedDto;
import com.april.furnitureapi.web.dto.furniture.FurnitureDto;
import com.april.furnitureapi.web.dto.furniture.FurnitureUpdateDto;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FurnitureMapper {
    @Mapping(target = "price", expression = "java(Long.parseLong(furnitureCreationDto.getPrice()))")
    Furniture funitureCreationToFurniture(FurnitureCreationDto furnitureCreationDto);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Furniture partialUpdate(FurnitureUpdateDto updateDto, @MappingTarget Furniture furniture);
    FurnitureDto furnitureToFurnitureDto(Furniture furniture);
    FurnitureDetailedDto furnitureToDetailedDto(Furniture furniture);
}
