package com.april.furnitureapi.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "furniture")
@Getter
@Setter
public class Furniture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "price")
    private Long price;
    @Column(name = "title")
    private String title;
    @Column(name = "description")
    private String description;

    @Column(name = "rating")
    private String rating;

}
