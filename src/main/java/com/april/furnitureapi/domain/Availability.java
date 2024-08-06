package com.april.furnitureapi.domain;

import com.april.furnitureapi.exception.InvalidAvailabilityStatus;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.io.IOException;

@JsonDeserialize(using = Availability.AvailabilityDeserializer.class)
public enum Availability {
    INSTOCK, OUTSTOCK, ONCOMING;

    public static Availability convert(String availability) {
        try {
            return Availability.valueOf(availability.toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new InvalidAvailabilityStatus(
                    "This status is unavailable %s".formatted(availability));
        }
    }

    public static class AvailabilityDeserializer extends JsonDeserializer<Availability> {
        @Override
        public Availability deserialize(JsonParser p, DeserializationContext ctxt)
                throws IOException {
            String value = p.getText().toUpperCase();
            try {
                return Availability.valueOf(value);
            } catch (IllegalArgumentException e) {
                throw new InvalidAvailabilityStatus("Invalid availability status: " + value);
            }
        }
    }
}
