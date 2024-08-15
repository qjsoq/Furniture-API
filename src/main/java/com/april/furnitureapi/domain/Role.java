package com.april.furnitureapi.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;
    @Column(unique = true)
    String name;

    @ManyToMany(mappedBy = "roles")
    @JsonIgnore
    Set<User> users = new HashSet<>();

    public static class GrantedAuthorityDeserializer extends JsonDeserializer<GrantedAuthority> {
        @Override
        public GrantedAuthority deserialize(JsonParser jsonParser,
                                            DeserializationContext deserializationContext)
                throws IOException {
            String authority = jsonParser.getText();
            return new SimpleGrantedAuthority(authority);
        }


    }

}
