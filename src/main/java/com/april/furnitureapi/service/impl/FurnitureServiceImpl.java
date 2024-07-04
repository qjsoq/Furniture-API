package com.april.furnitureapi.service.impl;

import com.april.furnitureapi.data.FurnitureRepository;
import com.april.furnitureapi.data.UserRepository;
import com.april.furnitureapi.domain.Furniture;
import com.april.furnitureapi.service.FurnitureService;
import com.april.furnitureapi.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class FurnitureServiceImpl implements FurnitureService {
    private FurnitureRepository furnitureRepository;
    private UserService userService;
    @Override
    public Furniture saveFurniture(Furniture furniture, String email) {
        furniture.setCreator(userService.findByEmail(email));
        return furnitureRepository.save(furniture);
    }
}
