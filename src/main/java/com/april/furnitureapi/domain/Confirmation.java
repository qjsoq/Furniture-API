package com.april.furnitureapi.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.UUID;

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
    public Confirmation(User user){
        this.user = user;
        creationTime = LocalDateTime.now();
        token = UUID.randomUUID().toString();
    }
}
