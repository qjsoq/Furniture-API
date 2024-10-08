package com.april.furnitureapi.web.controller;

import static com.april.furnitureapi.web.WebConstants.API;
import static com.april.furnitureapi.web.WebConstants.FURNITURE;

import com.april.furnitureapi.domain.FurnitureCategory;
import com.april.furnitureapi.domain.FurnitureDomain;
import com.april.furnitureapi.service.CommentService;
import com.april.furnitureapi.service.FurnitureService;
import com.april.furnitureapi.web.dto.comment.CommentCreationDto;
import com.april.furnitureapi.web.dto.comment.CommentDto;
import com.april.furnitureapi.web.dto.furniture.FurnitureCreationDto;
import com.april.furnitureapi.web.dto.furniture.FurnitureDetailedDto;
import com.april.furnitureapi.web.dto.furniture.FurnitureDto;
import com.april.furnitureapi.web.dto.furniture.FurnitureUpdateDto;
import com.april.furnitureapi.web.mapper.CommentMapper;
import com.april.furnitureapi.web.mapper.FurnitureMapper;
import jakarta.validation.Valid;
import java.net.URI;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping(path = API + FURNITURE, produces = MediaType.APPLICATION_JSON_VALUE)
public class FurnitureController {
    FurnitureMapper furnitureMapper;
    FurnitureService furnitureService;
    CommentService commentService;
    CommentMapper commentMapper;

    @PostMapping(value = {"", "/{availability}"})
    @PreAuthorize("@isUserVerified.isEmailVerified(authentication.name) and hasRole('ROLE_ADMIN')")
    public ResponseEntity<FurnitureDto> saveFurniture(
            @PathVariable(required = false) String availability,
            @RequestBody @Valid FurnitureCreationDto creationDto,
            Principal principal) {
        var furniture = furnitureService.saveFurniture(
                furnitureMapper.funitureCreationToFurniture(creationDto),
                principal.getName(), creationDto.getAmount(), creationDto.getWarehouseId());
        return ResponseEntity.created(URI.create(""))
                .body(furnitureMapper.furnitureToFurnitureDto(furniture));
    }

    @GetMapping
    public ResponseEntity<List<FurnitureDto>> getAllFurniture() {
        var allFurniture = furnitureService.findAll().stream()
                .map(furnitureMapper::furnitureToFurnitureDto)
                .toList();
        return ResponseEntity.ok(allFurniture);
    }

    @PostMapping("/comments/{vendorCode}")
    @PreAuthorize("@commentChecker.isUserAllowedToLeaveComment(#vendorCode, #principal.name)")
    public ResponseEntity<CommentDto> addComment(@PathVariable String vendorCode,
                                                 @RequestBody @Valid CommentCreationDto creationDto,
                                                 Principal principal) {
        var comment =
                commentService.create(commentMapper.creationDtoToComment(creationDto), vendorCode,
                        principal.getName());
        return ResponseEntity.created(URI.create(""))
                .body(commentMapper.commentToDto(comment));
    }

    @GetMapping("/comments/{vendorCode}")
    public ResponseEntity<List<CommentDto>> getComments(@PathVariable String vendorCode) {
        return ResponseEntity.ok(
                furnitureService.getComments(vendorCode).stream()
                        .map(commentMapper::commentToDto)
                        .toList());
    }

    @GetMapping("/{vendorCode}")
    public ResponseEntity<FurnitureDetailedDto> getFurnitureByVendorCode(
            @PathVariable String vendorCode) {
        return ResponseEntity.ok(
                furnitureMapper.furnitureToDetailedDto(
                        furnitureService.findByVendorCode(vendorCode))
        );
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<FurnitureDto>> getFurnitureByCategory(
            @PathVariable FurnitureCategory category) {
        return ResponseEntity.ok(
                furnitureService.findByCategory(category).stream()
                        .map(furnitureMapper::furnitureToFurnitureDto)
                        .toList()
        );
    }

    @GetMapping(value = {"/{domain}/{category}", "/{domain}/{category}/{sortBy}"})
    public ResponseEntity<List<FurnitureDto>> getFurnitureByDomain(
            @PathVariable FurnitureDomain domain,
            @PathVariable FurnitureCategory category,
            @PathVariable(required = false) String sortBy) {
        return ResponseEntity.ok(
                furnitureService.findByDomainAndCategory(category, domain,
                                Optional.ofNullable(sortBy)).stream()
                        .map(furnitureMapper::furnitureToFurnitureDto)
                        .toList()
        );
    }

    @PatchMapping("/update/{vendorCode}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<FurnitureDetailedDto> updateFurniture(@PathVariable String vendorCode,
                                                                @RequestBody
                                                                FurnitureUpdateDto furnitureUpdateDto) {
        var updatedFurniture =
                furnitureService.update(furnitureMapper.partialUpdate(furnitureUpdateDto,
                        furnitureService.findByVendorCode(vendorCode)));
        return ResponseEntity.ok(furnitureMapper.furnitureToDetailedDto(updatedFurniture));
    }

    @DeleteMapping("/delete/{vendorCode}")
    @PreAuthorize("@furnitureChecker.checkId(#vendorCode) and hasRole('ROLE_ADMIN')")
    @Transactional
    public ResponseEntity<Void> deleteFurniture(@PathVariable String vendorCode) {
        furnitureService.deleteFurniture(vendorCode);
        return ResponseEntity.noContent().build();
    }
}
