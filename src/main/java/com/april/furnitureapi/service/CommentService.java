package com.april.furnitureapi.service;

import com.april.furnitureapi.domain.Comment;

public interface CommentService {
    Comment create(Comment comment, String vendorCode, String email);
}
