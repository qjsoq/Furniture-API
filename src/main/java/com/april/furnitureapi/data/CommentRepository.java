package com.april.furnitureapi.data;

import com.april.furnitureapi.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CommentRepository extends JpaRepository<Comment, UUID> {
}
