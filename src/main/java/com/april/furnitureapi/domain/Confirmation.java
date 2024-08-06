package com.april.furnitureapi.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@Table(name = "confirmations")
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
public class Confirmation {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false)
    UUID id;
    @Column(name = "creation_time")
    LocalDateTime creationTime;
    String token;
    @OneToOne(targetEntity = User.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    public Confirmation(User user) {
        this.user = user;
        creationTime = LocalDateTime.now();
        token = UUID.randomUUID().toString();
    }
}
