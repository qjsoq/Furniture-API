package com.april.furnitureapi.web.mapper;

import com.april.furnitureapi.domain.User;
import com.april.furnitureapi.web.dto.user.UserCreationDto;
import com.april.furnitureapi.web.dto.user.UserDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface Usermapper {
    User toEntity(UserCreationDto creationDto);
    UserDto toPayload(User user);
}
