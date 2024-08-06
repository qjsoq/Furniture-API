package com.april.furnitureapi.web;

import com.april.furnitureapi.exception.CartNotFoundException;
import com.april.furnitureapi.exception.CommentNotFoundException;
import com.april.furnitureapi.exception.FurnitureNotFoundException;
import com.april.furnitureapi.exception.InvalidAvailabilityStatus;
import com.april.furnitureapi.exception.InvalidCategoryValueException;
import com.april.furnitureapi.exception.InvalidDomainValueException;
import com.april.furnitureapi.exception.InvalidPasswordException;
import com.april.furnitureapi.exception.InvalidTokenException;
import com.april.furnitureapi.exception.UserAlreadyExistsException;
import com.april.furnitureapi.exception.UserIsNotVerifiedException;
import com.april.furnitureapi.exception.UserNotFoundException;
import com.april.furnitureapi.exception.VendorCodeAlreadyExists;
import com.april.furnitureapi.exception.WarehouseNotFoundException;
import com.april.furnitureapi.web.dto.ErrorResponse;
import com.auth0.jwt.exceptions.JWTVerificationException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CentralizedExceptionHandler {
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<Map<String, List<String>>> argumentNotValid(
            MethodArgumentNotValidException exception) {
        Map<String, List<String>> errors = exception.getFieldErrors()
                .stream()
                .collect(Collectors.groupingBy(FieldError::getField,
                        Collectors.mapping(FieldError::getDefaultMessage, Collectors.toList())));
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler({InvalidPasswordException.class, UserAlreadyExistsException.class,
            InvalidTokenException.class,
            VendorCodeAlreadyExists.class, InvalidCategoryValueException.class,
            InvalidDomainValueException.class,
            InvalidAvailabilityStatus.class})
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ErrorResponse InvalidParametersExceptionHandler(RuntimeException runtimeException) {
        return new ErrorResponse(runtimeException.getLocalizedMessage());
    }

    @ExceptionHandler({UserNotFoundException.class, FurnitureNotFoundException.class,
            CommentNotFoundException.class,
            CartNotFoundException.class, WarehouseNotFoundException.class})
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public ErrorResponse NotFoundExceptionHandler(RuntimeException runtimeException) {
        return new ErrorResponse(runtimeException.getMessage());
    }

    @ExceptionHandler({JWTVerificationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse JwtVerificationExceptionHandler(RuntimeException runtimeException) {
        return new ErrorResponse(runtimeException.getMessage() + " provided token not valid(");
    }

    @ExceptionHandler({UserIsNotVerifiedException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse userVerificationHandler(RuntimeException runtimeException) {
        return new ErrorResponse(runtimeException.getMessage());
    }
}
