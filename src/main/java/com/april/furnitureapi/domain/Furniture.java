package com.april.furnitureapi.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.security.core.GrantedAuthority;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Data
@Entity
@Table(name = "furniture")
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize(using = Furniture.FurnitureDeserializer.class)
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

    public static class FurnitureKeyDeserializer extends KeyDeserializer {
        @Override
        public Furniture deserializeKey(String key, DeserializationContext ctxt) throws IOException {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(key, Furniture.class);
        }
    }

    public static class FurnitureDeserializer extends JsonDeserializer<Furniture> {
        @Override
        public Furniture deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
            ObjectCodec codec = jsonParser.getCodec();
            JsonNode node = codec.readTree(jsonParser);

            int id = node.get("id").asInt();
            int price = node.get("price").asInt();
            String title = node.get("title").asText();
            String description = node.get("description").asText();
            String rating = node.get("rating").asText();
            String vendorCode = node.get("vendorCode").asText();
            int numberOfReviews = node.get("numberOfReviews").asInt();
            LocalDateTime createdAt = LocalDateTime.parse(node.get("createdAt").asText());

            User creator = codec.treeToValue(node.get("creator"), User.class);
            FurnitureCategory category = FurnitureCategory.valueOf(node.get("category").asText());
            FurnitureDomain domain = FurnitureDomain.valueOf(node.get("domain").asText());
            Availability availability = Availability.valueOf(node.get("availability").asText());
            List<Comment> comments = new ArrayList<>();
            for (JsonNode commentNode : node.get("comments")) {
                Comment comment = codec.treeToValue(commentNode, Comment.class);
                comments.add(comment);
            }

            return new Furniture((long) id, (long) price, title, description, rating, vendorCode, numberOfReviews, createdAt, creator, category, domain, availability, comments);
        }
    }
}
