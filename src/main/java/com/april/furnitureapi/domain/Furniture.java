package com.april.furnitureapi.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    @CreationTimestamp
    @Column(name = "created_at")
    LocalDateTime createdAt;
    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "creator_id")
    User creator;
    @Enumerated(EnumType.STRING)
    @Column(name = "category")
    FurnitureCategory category;
    @Enumerated(EnumType.STRING)
    @Column(name = "domain")
    FurnitureDomain domain;
    @Enumerated(EnumType.STRING)
    @Column(name = "availability")
    Availability availability;
    @OneToMany(mappedBy = "furniture", cascade = CascadeType.REMOVE, orphanRemoval = true)
    List<Comment> comments = new ArrayList<>();
}
