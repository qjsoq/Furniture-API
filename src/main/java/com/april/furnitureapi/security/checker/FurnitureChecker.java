package com.april.furnitureapi.security.checker;

import com.april.furnitureapi.data.FurnitureRepository;
import com.april.furnitureapi.exception.FurnitureNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FurnitureChecker {
    private final FurnitureRepository furnitureRepository;
    public boolean checkId(String vendorCode){
        if(!furnitureRepository.existsByVendorCode(vendorCode)) throw new FurnitureNotFoundException(
                "Furniture with provided vendor code %s does not exist".formatted(vendorCode)
        );
        return true;
    }
}
