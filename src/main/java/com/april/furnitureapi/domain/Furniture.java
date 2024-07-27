package com.april.furnitureapi.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
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
@AllArgsConstructor
@NoArgsConstructor
public class Furniture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(name = "price")
    @JsonProperty("price")
    Long price;
    @Column(name = "title")
    @JsonProperty("title")
    String title;
    @Column(name = "description")
    @JsonProperty("description")
    String description;
    @Column(name = "rating")
    @JsonProperty("rating")
    String rating;
    @Column(name = "vendor_code")
    @JsonProperty("vendorCode")
    String vendorCode;
    @Column(name = "number_of_reviews")
    @JsonProperty("number_of_reviews")
    Integer numberOfReviews;
    @CreationTimestamp
    @Column(name = "created_at")
    @JsonProperty("created_at")
    LocalDateTime createdAt;
    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "creator_id")
    @JsonProperty("creator_id")
    User creator;
    @Enumerated(EnumType.STRING)
    @Column(name = "category")
    @JsonProperty("category")
    FurnitureCategory category;
    @Enumerated(EnumType.STRING)
    @Column(name = "domain")
    @JsonProperty("domain")
    FurnitureDomain domain;
    @Enumerated(EnumType.STRING)
    @Column(name = "availability")
    @JsonProperty("availability")
    Availability availability;
    @OneToMany(mappedBy = "furniture", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JsonProperty("comments")
    List<Comment> comments = new ArrayList<>();
}
