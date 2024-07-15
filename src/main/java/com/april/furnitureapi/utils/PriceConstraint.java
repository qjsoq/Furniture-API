package com.april.furnitureapi.utils;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PriceValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface PriceConstraint {
    String message() default "Provided price is not valid";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
