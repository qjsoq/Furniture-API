package com.april.furnitureapi.exception;

public class InvalidAvailabilityStatus extends RuntimeException{
    public InvalidAvailabilityStatus(String message){
        super(message);
    }
}
