package com.april.furnitureapi.service;

import com.april.furnitureapi.domain.Furniture;

public interface FurnitureService {
    Furniture saveFurniture(Furniture furniture, String email);
}
