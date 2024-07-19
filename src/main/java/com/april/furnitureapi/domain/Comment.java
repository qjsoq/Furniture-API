package com.april.furnitureapi.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Entity
@Table(name = "comments")
@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    Long id;
    @ManyToOne
    @JoinColumn(name = "author_id")
    User author;
    @Column(name = "rating")
    Double rating;
    @Column(name = "content")
    String content;
    @ManyToOne
    @JoinColumn(name = "furniture_id")
    Furniture furniture;
}
