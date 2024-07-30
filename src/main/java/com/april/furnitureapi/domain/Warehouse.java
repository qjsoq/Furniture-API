package com.april.furnitureapi.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Map;

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
    @ElementCollection
    @CollectionTable(name = "warehouse_storage", joinColumns = @JoinColumn(name = "warehouse_id"))
    @MapKeyJoinColumn(name = "furniture_id")
    @Column(name = "quantity")
    Map<Furniture, Integer> storage;
}
