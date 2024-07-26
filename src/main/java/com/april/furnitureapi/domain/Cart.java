package com.april.furnitureapi.domain;

import com.april.furnitureapi.utils.FurnitureMapDeserializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.IOException;
import java.util.Map;
@Data
@Entity
@Table(name = "cart")
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(name = "price")
    @JsonProperty("price")
    Long price;
    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "creator_id")
    @JsonProperty("creator")
    User creator;
    @JsonSerialize(using = FurnitureMapSerializer.class)
    @JsonDeserialize(using = FurnitureMapDeserializer.class)
    @ElementCollection
    @CollectionTable(name = "furniture_quantity_mapping", joinColumns = @JoinColumn(name = "cart_id"))
    @MapKeyJoinColumn(name = "furniture_id")
    @Column(name = "quantity")
    Map<Furniture, Integer> items;


    public static class FurnitureMapSerializer extends JsonSerializer<Map<Furniture, Integer>> {
        @Override
        public void serialize(Map<Furniture, Integer> furnitureMap, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeStartObject();
            for (Map.Entry<Furniture, Integer> entry : furnitureMap.entrySet()) {
                gen.writeFieldName(entry.getKey().getId().toString());  // Assuming Furniture has an getId() method
                gen.writeNumber(entry.getValue());
            }
            gen.writeEndObject();
        }
    }




}
