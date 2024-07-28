package com.april.furnitureapi.security.checker;

import com.april.furnitureapi.data.CommentRepository;
import com.april.furnitureapi.data.UserRepository;
import com.april.furnitureapi.exception.CommentNotFoundException;
import com.april.furnitureapi.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CommentChecker {
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final IsUserVerified verified;
    public boolean isUserTheAuthor(String email, Long commentId){
        var user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException(
                "User with provided email %s doesnt exist".formatted(email)
        ));
        var comment = commentRepository.findById(commentId).orElseThrow(() -> new CommentNotFoundException(
                "Comment with provided id %s doesnt exist".formatted(commentId)
        ));
        return comment.getAuthor().getUsername().equals(user.getUsername()) && verified.isEmailVerified(email);
    }
    public boolean isUserAllowedToLeaveComment(String vendorCode, String email){
        return commentRepository.isUserAllowedToLeaveComment(email, vendorCode);
    }
}
