package com.april.furnitureapi.domain;

import com.april.furnitureapi.utils.FurnitureMapDeserializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapKeyJoinColumn;
import jakarta.persistence.Table;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "cart")
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(name = "price")
    @JsonProperty("price")
    Long price;
    @Column(name = "cart_code")
    @JsonProperty("cartCode")
    String cartCode;
    @CreationTimestamp
    @Column(name = "created_at")
    @JsonProperty("created_at")
    LocalDateTime createdAt;
    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "creator_id")
    @JsonProperty("creator")
    User creator;
    @JsonSerialize(using = FurnitureMapSerializer.class)
    @JsonDeserialize(using = FurnitureMapDeserializer.class)
    @ElementCollection
    @CollectionTable(name = "cart_items", joinColumns = @JoinColumn(name = "cart_id"))
    @MapKeyJoinColumn(name = "furniture_id")
    @Column(name = "quantity")
    Map<Furniture, Integer> items;


    public static class FurnitureMapSerializer extends JsonSerializer<Map<Furniture, Integer>> {
        @Override
        public void serialize(Map<Furniture, Integer> furnitureMap, JsonGenerator gen,
                              SerializerProvider serializers) throws IOException {
            gen.writeStartObject();
            for (Map.Entry<Furniture, Integer> entry : furnitureMap.entrySet()) {
                gen.writeFieldName(entry.getKey().getId().toString());
                gen.writeNumber(entry.getValue());
            }
            gen.writeEndObject();
        }
    }
}
