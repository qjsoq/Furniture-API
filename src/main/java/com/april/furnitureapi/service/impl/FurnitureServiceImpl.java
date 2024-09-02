package com.april.furnitureapi.service.impl;

import com.april.furnitureapi.data.FurnitureRepository;
import com.april.furnitureapi.data.WarehouseRepository;
import com.april.furnitureapi.domain.Availability;
import com.april.furnitureapi.domain.Comment;
import com.april.furnitureapi.domain.Furniture;
import com.april.furnitureapi.domain.FurnitureCategory;
import com.april.furnitureapi.domain.FurnitureDomain;
import com.april.furnitureapi.exception.FurnitureNotFoundException;
import com.april.furnitureapi.exception.VendorCodeAlreadyExists;
import com.april.furnitureapi.exception.WarehouseNotFoundException;
import com.april.furnitureapi.service.FurnitureService;
import com.april.furnitureapi.service.UserService;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class FurnitureServiceImpl implements FurnitureService {
    private FurnitureRepository furnitureRepository;
    private UserService userService;
    private WarehouseRepository warehouseRepository;

    private static List<Furniture> initialSorting(List<Furniture> unsortedList) {
        Map<Boolean, List<Furniture>> availabilityMap = unsortedList.stream()
                .collect(Collectors.partitioningBy(
                        (furniture -> furniture.getAvailability().equals(Availability.OUTSTOCK))
                ));
        List<Furniture> filteredList = new ArrayList<>(availabilityMap.get(false));
        filteredList.addAll(availabilityMap.get(true));
        return filteredList;
    }

    private static Sort createSortObject(String sortBy) {
        return switch (sortBy) {
            case "cheap" -> Sort.by(Sort.Direction.ASC, "price");
            case "expensive" -> Sort.by(Sort.Direction.DESC, "price");
            default -> Sort.by(Sort.Direction.DESC, "createdAt");
        };
    }

    @Override
    public Furniture saveFurniture(Furniture furniture, String email, Integer amount,
                                   Long warehouseId) {
        var warehouse = warehouseRepository.findById(warehouseId)
                .orElseThrow(() -> new WarehouseNotFoundException(
                        "Warehouse was not found"
                ));
        var expectedAvailability = (amount == 0 ? Availability.ONCOMING : Availability.INSTOCK);
        furniture.setVendorCode(
                String.valueOf(new Random().nextInt(9999999 - 1000000 + 1) + 1000000));
        if (furnitureRepository.existsByVendorCode(furniture.getVendorCode())) {
            throw new VendorCodeAlreadyExists(
                    "The random vendor code %s has already been used, try to save the furniture one more time."
                            .formatted(furniture.getVendorCode()));
        }
        furniture.setCreator(userService.findByEmail(email));
        furniture.setAvailability(expectedAvailability);
        furniture.setNumberOfReviews(0);

        warehouse.addFurniture(furniture, amount);
        furnitureRepository.save(furniture);
        warehouseRepository.save(warehouse);
        return furniture;
    }

    @Override
    public List<Furniture> findAll() {
        return furnitureRepository.findAll();
    }

    @Override
    public Furniture findByVendorCode(String vendorCode) {
        return furnitureRepository.findByVendorCode(vendorCode)
                .orElseThrow(() -> new FurnitureNotFoundException(
                        "Furniture with this vendor code %s doesnt exist".formatted(vendorCode)
                ));
    }

    @Override
    public List<Furniture> findByCategory(FurnitureCategory category) {
        return furnitureRepository.findByCategory(category);
    }

    @Override
    public List<Furniture> findByDomainAndCategory(FurnitureCategory category,
                                                   FurnitureDomain domain,
                                                   Optional<String> sortBy) {
        return initialSorting(furnitureRepository.findByDomainAndCategory(domain, category,
                createSortObject(sortBy.orElse("novelty"))));
    }

    @Override
    public List<Comment> getComments(String vendorCode) {
        var furniture = findByVendorCode(vendorCode);
        return furniture.getComments();
    }

    @Override
    public Furniture update(Furniture furniture) {
        return furnitureRepository.save(furniture);
    }

    @Override
    public void deleteFurniture(String vendorCode) {
        furnitureRepository.deleteByVendorCode(vendorCode);
    }
}
