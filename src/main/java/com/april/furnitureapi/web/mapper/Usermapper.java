package com.april.furnitureapi.web.mapper;

import com.april.furnitureapi.domain.User;
import com.april.furnitureapi.web.dto.user.UserCreationDto;
import com.april.furnitureapi.web.dto.user.UserDto;
import com.april.furnitureapi.web.dto.user.UserUpdateDto;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface Usermapper {
    User toEntity(UserCreationDto creationDto);
    UserDto toPayload(User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    User partialUpdate(UserUpdateDto userUpdate, @MappingTarget User user);
}
