package com.april.furnitureapi.domain;

import com.april.furnitureapi.exception.InvalidCategoryValueException;
import com.april.furnitureapi.exception.InvalidDomainValueException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.io.IOException;

@JsonDeserialize(using = FurnitureDomain.FurnitureDomainDeserializer.class)
public enum FurnitureDomain {
    KITCHEN, BEDROOM, SITTINGROOM,
    LIVINGROOM, BATHROOM, STUDY;
    public static class FurnitureDomainDeserializer extends JsonDeserializer<FurnitureDomain> {
        @Override
        public FurnitureDomain deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            String value = p.getText().toUpperCase();
            try {
                return FurnitureDomain.valueOf(value);
            } catch (IllegalArgumentException e) {
                throw new InvalidDomainValueException("Invalid category: " + value);
            }
        }
    }
}
