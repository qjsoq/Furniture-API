package com.april.furnitureapi.domain;

import com.april.furnitureapi.exception.InvalidAvailabilityStatus;

public enum Availability {
    INSTOCK, OUTSTOCK, ONCOMING;

    public static Availability convert(String availability){
        try{
            return Availability.valueOf(availability.toUpperCase());
        } catch (IllegalArgumentException ex){
            throw new InvalidAvailabilityStatus("This status is unavailable %s".formatted(availability));
        }
    }
}
