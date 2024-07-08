package com.april.furnitureapi.exception;

public class VendorCodeAlreadyExists extends RuntimeException{
    public VendorCodeAlreadyExists(String message){
        super(message);
    }
}
