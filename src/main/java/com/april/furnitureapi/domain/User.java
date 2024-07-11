package com.april.furnitureapi.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;


@Entity
@Table(name = "USERS")
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false)
    UUID id;
    @Column(nullable = false)
    String name;
    @Column(name = "last_name")
    String lastname;
    String username;
    String email;
    String password;
    @Column(name = "verified")
    boolean isVerified;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
            name = "role_id", referencedColumnName = "id"))
    Set<Role> roles = new HashSet<>();
}
