package com.april.furnitureapi.service.impl;

import com.april.furnitureapi.data.CommentRepository;
import com.april.furnitureapi.domain.Comment;
import com.april.furnitureapi.service.CommentService;
import com.april.furnitureapi.service.FurnitureService;
import com.april.furnitureapi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final UserService userService;
    private final FurnitureService furnitureService;
    private final CommentRepository commentRepository;
    @Override
    public Comment create(Comment comment, String vendorCode, String email) {
        comment.setAuthor(userService.findByEmail(email));
        comment.setFurniture(furnitureService.findByVendorCode(vendorCode));
        return commentRepository.save(comment);
    }
}
