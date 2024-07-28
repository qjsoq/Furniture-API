package com.april.furnitureapi.web.mapper;

import com.april.furnitureapi.domain.Warehouse;
import com.april.furnitureapi.web.dto.warehouse.WarehouseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface WarehouseMapper {
    @Mapping(target = "street", expression = "java(warehouse.getAddress().getStreet())")
    @Mapping(target = "houseNo", expression = "java(warehouse.getAddress().getHouseNo())")
    WarehouseDto toDto(Warehouse warehouse);
}
