package com.april.furnitureapi.exception;

public class VendorCodeNotFoundException extends RuntimeException{
    public VendorCodeNotFoundException(String message){
        super(message);
    }
}
