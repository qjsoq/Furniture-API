package com.april.furnitureapi.exception;

public class UserIsNotVerifiedException extends RuntimeException{
    public UserIsNotVerifiedException(String email){
        super(email);
    }
}
