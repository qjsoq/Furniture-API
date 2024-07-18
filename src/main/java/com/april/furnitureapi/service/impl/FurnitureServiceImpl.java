package com.april.furnitureapi.service.impl;

import com.april.furnitureapi.data.FurnitureRepository;
import com.april.furnitureapi.domain.Availability;
import com.april.furnitureapi.domain.Furniture;
import com.april.furnitureapi.domain.FurnitureCategory;
import com.april.furnitureapi.domain.FurnitureDomain;
import com.april.furnitureapi.exception.VendorCodeAlreadyExists;
import com.april.furnitureapi.service.FurnitureService;
import com.april.furnitureapi.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class FurnitureServiceImpl implements FurnitureService {
    private FurnitureRepository furnitureRepository;
    private UserService userService;
    @Override
    @PreAuthorize("@isUserVerified.isEmailVerified(authentication.name) and hasRole('ROLE_ADMIN')")
    public Furniture saveFurniture(Furniture furniture, String email, Optional<String> availability) {
        var expectedAvailability = availability
                .flatMap(
                        (avail) -> Optional.of(Availability.convert(avail)))
                .orElse(Availability.INSTOCK);
        furniture.setVendorCode(String.valueOf(new Random().nextInt(9999999 - 1000000 + 1) + 1000000));
        if(furnitureRepository.existsByVendorCode(furniture.getVendorCode())){
            throw new VendorCodeAlreadyExists("The random vendor code %s has already been used, try to save the furniture one more time.".formatted(furniture.getVendorCode()));
        }
        furniture.setCreator(userService.findByEmail(email));
        furniture.setAvailability(expectedAvailability);
        return furnitureRepository.save(furniture);
    }

    @Override
    public List<Furniture> findAll() {
        return furnitureRepository.findAll();
    }

    @Override
    public Furniture findByVendorCode(String vendorCode) {
        return furnitureRepository.findByVendorCode(vendorCode);
    }

    @Override
    public List<Furniture> findByCategory(FurnitureCategory category) {
        return furnitureRepository.findByCategory(category);
    }

    @Override
    public List<Furniture> findByDomainAndCategory(FurnitureCategory category, FurnitureDomain domain, Optional<String> sortBy) {
        return initialSorting(furnitureRepository.findByDomainAndCategory(domain, category, createSortObject(sortBy.orElse("novelty"))));
    }

    private static List<Furniture> initialSorting(List<Furniture> unsortedList){
        Map<Boolean, List<Furniture>> availabilityMap = unsortedList.stream()
                .collect(Collectors.partitioningBy(
                        (furniture -> furniture.getAvailability().equals(Availability.OUTSTOCK))
                ));
        List<Furniture> filteredList = new ArrayList<>(availabilityMap.get(false));
        filteredList.addAll(availabilityMap.get(true));
        return  filteredList;
    }
    private static Sort createSortObject(String sortby){
        return switch (sortby) {
            case "cheap" -> Sort.by(Sort.Direction.ASC, "price");
            case "expensive" -> Sort.by(Sort.Direction.DESC, "price");
            default -> Sort.by(Sort.Direction.DESC, "createdAt");
        };
    }
}
