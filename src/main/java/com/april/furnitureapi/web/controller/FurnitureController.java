package com.april.furnitureapi.web.controller;

import com.april.furnitureapi.service.FurnitureService;
import com.april.furnitureapi.web.dto.furniture.FurnitureCreationDto;
import com.april.furnitureapi.web.dto.furniture.FurnitureDto;
import com.april.furnitureapi.web.mapper.FurnitureMapper;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.security.Principal;

import static com.april.furnitureapi.web.WebConstants.API;

@RestController
@AllArgsConstructor
@RequestMapping(path = API + "/furniture",  produces = MediaType.APPLICATION_JSON_VALUE)
public class FurnitureController {
    FurnitureMapper furnitureMapper;
    FurnitureService furnitureService;
    @PostMapping
    public ResponseEntity<FurnitureDto> saveFurniture(@RequestBody @Valid FurnitureCreationDto creationDto,
                                                      Principal principal){
        var furniture = furnitureService.saveFurniture(furnitureMapper.funitureCreationToFurniture(creationDto), principal.getName());
        return ResponseEntity.created(URI.create("")).
                body(furnitureMapper.furnitureToFurnitureDto(furniture));
    }
}
