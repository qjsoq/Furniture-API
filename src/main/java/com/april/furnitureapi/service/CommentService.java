package com.april.furnitureapi.service;

import com.april.furnitureapi.domain.Comment;

import java.util.UUID;

public interface CommentService {
    Comment create(Comment comment, String vendorCode, String email);
    void delete(Long id);
}
