package com.april.furnitureapi.service.impl;

import com.april.furnitureapi.data.CommentRepository;
import com.april.furnitureapi.data.FurnitureRepository;
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
    private final FurnitureRepository furnitureRepository;

    @Override
    public Comment create(Comment comment, String vendorCode, String email) {
        comment.setAuthor(userService.findByEmail(email));
        var furniture = furnitureService.findByVendorCode(vendorCode);
        comment.setFurniture(updateRating(comment.getRating(), furniture));
        furnitureRepository.save(furniture);
        return commentRepository.save(comment);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        commentRepository.deleteById(id);
    }

    private Furniture updateRating(Double rating, Furniture furniture) {
        int numberOfReviews = furniture.getNumberOfReviews();
        if (numberOfReviews == 0) {
            furniture.setRating(String.valueOf(rating));
        } else {
            double currentRating = Double.parseDouble(furniture.getRating());
            double newRating = ((numberOfReviews * currentRating) + rating) / (numberOfReviews + 1);
            furniture.setRating(String.format("%.1f", newRating));
        }
        furniture.setNumberOfReviews(numberOfReviews + 1); // Increment number of reviews
        return furniture;
    }
}
