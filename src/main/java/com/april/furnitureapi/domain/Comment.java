package com.april.furnitureapi.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

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

    @PrePersist
    private void updateNumberOfReviews() {
        this.furniture.setNumberOfReviews(furniture.getComments().size() + 1);
    }
}
