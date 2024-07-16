package com.april.furnitureapi.service.impl;

import com.april.furnitureapi.data.FurnitureRepository;
import com.april.furnitureapi.data.UserRepository;
import com.april.furnitureapi.domain.Furniture;
import com.april.furnitureapi.domain.FurnitureCategory;
import com.april.furnitureapi.exception.VendorCodeAlreadyExists;
import com.april.furnitureapi.service.FurnitureService;
import com.april.furnitureapi.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@AllArgsConstructor
public class FurnitureServiceImpl implements FurnitureService {
    private FurnitureRepository furnitureRepository;
    private UserService userService;
    @Override
    @PreAuthorize("@isUserVerified.isEmailVerified(authentication.name) and hasRole('ROLE_ADMIN')")
    public Furniture saveFurniture(Furniture furniture, String email) {
        furniture.setCreator(userService.findByEmail(email));
        furniture.setVendorCode(String.valueOf(new Random().nextInt(9999999 - 1000000 + 1) + 1000000));
        if(furnitureRepository.existsByVendorCode(furniture.getVendorCode())){
            throw new VendorCodeAlreadyExists("The random vendor code %s has already been used, try to save the furniture one more time.".formatted(furniture.getVendorCode()));
        }
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
}
