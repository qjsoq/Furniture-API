package com.april.furnitureapi.exception;

public class FurnitureNotFoundException extends RuntimeException{
    public FurnitureNotFoundException(String message){
        super(message);
    }
}
