package com.april.furnitureapi.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "furniture")
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Furniture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(name = "price")
    Long price;
    @Column(name = "title")
    String title;
    @Column(name = "description")
    String description;
    @Column(name = "rating")
    String rating;
    @Column(name = "vendor_code")
    String vendorCode;
    @ManyToOne(targetEntity = User.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "creator_id")
    User creator;
    @Enumerated(EnumType.STRING)
    @Column(name = "category")
    FurnitureCategory category;
}
