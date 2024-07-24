package com.april.furnitureapi.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Map;

@Entity
@Table(name = "cart")
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@Data
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
    User creator;
    @ElementCollection
    @CollectionTable(name = "cart_items", joinColumns = @JoinColumn(name = "cart_id"))
    @MapKeyJoinColumn(name = "furniture_id")
    @Column(name = "quantity")
    Map<Furniture, Integer> items;


}
