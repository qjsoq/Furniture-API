package com.april.furnitureapi.service.impl;

import com.april.furnitureapi.data.CommentRepository;
import com.april.furnitureapi.domain.Comment;
import com.april.furnitureapi.domain.Furniture;
import com.april.furnitureapi.service.CommentService;
import com.april.furnitureapi.service.FurnitureService;
import com.april.furnitureapi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final UserService userService;
    private final FurnitureService furnitureService;
    private final CommentRepository commentRepository;
    @Override
    public Comment create(Comment comment, String vendorCode, String email) {
        comment.setAuthor(userService.findByEmail(email));
        var furniture = furnitureService.findByVendorCode(vendorCode);
        comment.setFurniture(updateReviews(comment.getRating(), furniture));
        return commentRepository.save(comment);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        commentRepository.deleteById(id);
    }

    private Furniture updateReviews(Double rating, Furniture furniture){
        int numberOfReviews = furniture.getNumberOfReviews();
        if(numberOfReviews == 0){
            furniture.setRating(String.valueOf(rating));
            return furniture;
        }
        System.out.println((numberOfReviews * Double.parseDouble(furniture.getRating())) + rating);
        double newRating = ((numberOfReviews * Double.parseDouble(furniture.getRating())) + rating) / (numberOfReviews + 1);
        furniture.setRating(String.format("%.1f", newRating));
        return furniture;
    }
}
