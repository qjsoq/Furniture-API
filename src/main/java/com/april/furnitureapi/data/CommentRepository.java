package com.april.furnitureapi.data;

import com.april.furnitureapi.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("SELECT COUNT (c) = 0 FROM Comment c WHERE c.author.email = :email AND c.furniture.vendorCode = :vendorCode")
    boolean isUserAllowedToLeaveComment(@Param("email") String email,
                                        @Param("vendorCode") String vendorCode);
}
