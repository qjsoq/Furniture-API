package com.april.furnitureapi.web.controller;

import com.april.furnitureapi.domain.FurnitureCategory;
import com.april.furnitureapi.domain.FurnitureDomain;
import com.april.furnitureapi.service.FurnitureService;
import com.april.furnitureapi.web.dto.furniture.FurnitureCreationDto;
import com.april.furnitureapi.web.dto.furniture.FurnitureDetailedDto;
import com.april.furnitureapi.web.dto.furniture.FurnitureDto;
import com.april.furnitureapi.web.mapper.FurnitureMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.net.URI;
import java.security.Principal;

import static com.april.furnitureapi.web.WebConstants.API;

@RestController
@AllArgsConstructor
@RequestMapping(path = API + "/furniture", produces = MediaType.APPLICATION_JSON_VALUE)
public class FurnitureController {
    FurnitureMapper furnitureMapper;
    FurnitureService furnitureService;

    @PostMapping
    public ResponseEntity<FurnitureDto> saveFurniture(@RequestBody @Valid FurnitureCreationDto creationDto,
                                                      Principal principal) {
        var furniture = furnitureService.saveFurniture(furnitureMapper.funitureCreationToFurniture(creationDto), principal.getName());
        return ResponseEntity.created(URI.create("")).
                body(furnitureMapper.furnitureToFurnitureDto(furniture));
    }

    @GetMapping
    public ResponseEntity<List<FurnitureDto>> getAllFurniture() {
        var allFurniture = furnitureService.findAll().stream()
                .map(furnitureMapper::furnitureToFurnitureDto)
                .toList();
        return ResponseEntity.ok(allFurniture);
    }

    @GetMapping("/{vendorCode}")
    public ResponseEntity<FurnitureDetailedDto> getFurnitureByVendorCode(@PathVariable String vendorCode) {
        return ResponseEntity.ok(
                furnitureMapper.furnitureToDetailedDto(furnitureService.findByVendorCode(vendorCode))
        );
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<FurnitureDto>> getFurnitureByCategory(@PathVariable FurnitureCategory category) {
        return ResponseEntity.ok(
                furnitureService.findByCategory(category).stream()
                        .map(furnitureMapper::furnitureToFurnitureDto)
                        .toList()
        );
    }

    @GetMapping("/{domain}/{category}")
    public ResponseEntity<List<FurnitureDto>> getFurnitureByDomain(@PathVariable FurnitureDomain domain,
                                                                   @PathVariable FurnitureCategory category) {
        return ResponseEntity.ok(
                furnitureService.findByDomainAndCategory(category, domain).stream()
                        .map(furnitureMapper::furnitureToFurnitureDto)
                        .toList()
        );
    }
}
