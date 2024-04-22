package com.april.furnitureapi.web.mapper;

import com.april.furnitureapi.web.dto.auth.AuthenticationResponse;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AuthenicationMapper {
    @Mapping(target = "expires", expression = "java(decodedJWT.getExpiresAt())")
    AuthenticationResponse toAuthResponse(DecodedJWT decodedJWT);
}
