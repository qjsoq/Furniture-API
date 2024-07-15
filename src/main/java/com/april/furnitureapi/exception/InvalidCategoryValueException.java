package com.april.furnitureapi.exception;

public class InvalidCategoryValueException extends RuntimeException{
    public InvalidCategoryValueException(String message){
        super(message);
    }
}
