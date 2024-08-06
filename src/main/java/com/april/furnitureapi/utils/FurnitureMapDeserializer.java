package com.april.furnitureapi.utils;

import com.april.furnitureapi.data.FurnitureRepository;
import com.april.furnitureapi.domain.Furniture;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class FurnitureMapDeserializer extends JsonDeserializer<Map<Furniture, Integer>> {
    FurnitureRepository userRepository;

    @Override
    public Map<Furniture, Integer> deserialize(JsonParser p, DeserializationContext ctxt)
            throws IOException {
        ObjectCodec codec = p.getCodec();
        ObjectNode node = codec.readTree(p);
        Iterator<Map.Entry<String, JsonNode>> fields = node.fields();
        Map<Furniture, Integer> furnitureMap = new HashMap<>();

        while (fields.hasNext()) {
            Map.Entry<String, JsonNode> field = fields.next();
            Long furnitureId = Long.valueOf(field.getKey());
            Integer quantity = codec.treeToValue(field.getValue(), Integer.class);

            Furniture furniture = getFurnitureById(furnitureId);
            furnitureMap.put(furniture, quantity);
        }

        return furnitureMap;
    }

    private Furniture getFurnitureById(Long id) {
        return userRepository.findById(id).get();
    }
}