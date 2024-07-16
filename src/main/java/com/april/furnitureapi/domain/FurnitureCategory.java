package com.april.furnitureapi.domain;

import com.april.furnitureapi.exception.InvalidCategoryValueException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;


import java.io.IOException;
@JsonDeserialize(using = FurnitureCategory.FurnitureCategoryDeserializer.class)
public enum FurnitureCategory {
    CHAIR, TABLE, COUCH,
    BED, ARMCHAIR, RECLINER,
    ARMOIRE;
    public static class FurnitureCategoryDeserializer extends JsonDeserializer<FurnitureCategory> {
        @Override
        public FurnitureCategory deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            String value = p.getText().toUpperCase();
            try {
                return FurnitureCategory.valueOf(value);
            } catch (IllegalArgumentException e) {
                throw new InvalidCategoryValueException("Invalid category: " + value);
            }
        }
    }
}
