package com.april.furnitureapi.web.mapper;

import com.april.furnitureapi.domain.Furniture;
import com.april.furnitureapi.web.dto.furniture.FurnitureCreationDto;
import com.april.furnitureapi.web.dto.furniture.FurnitureDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FurnitureMapper {
    Furniture funitureCreationToFurniture(FurnitureCreationDto furnitureCreationDto);
    FurnitureDto furnitureToFurnitureDto(Furniture furniture);
}
