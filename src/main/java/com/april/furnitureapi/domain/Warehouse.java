package com.april.furnitureapi.domain;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapKeyJoinColumn;
import jakarta.persistence.Table;
import java.util.HashMap;
import java.util.Map;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "warehouse")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Warehouse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "name")
    String name;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "address")
    Address address;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "warehouse_storage",
            joinColumns = @JoinColumn(name = "warehouse_id")
    )
    @MapKeyJoinColumn(name = "furniture_id")
    @Column(name = "quantity")
    Map<Furniture, Integer> storage = new HashMap<>();

    public void addFurniture(Furniture furniture, Integer amount) {
        if (storage == null) {
            storage = new HashMap<>();
        }

        this.storage.merge(furniture, amount, Integer::sum);
    }
}
