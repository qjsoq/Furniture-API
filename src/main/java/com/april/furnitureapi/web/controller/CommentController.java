package com.april.furnitureapi.web.controller;

import static com.april.furnitureapi.web.WebConstants.API;

import com.april.furnitureapi.service.CommentService;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = API + "/comments", produces = MediaType.APPLICATION_JSON_VALUE)
public class CommentController {
    private final CommentService commentService;

    @DeleteMapping("/{id}")
    @PreAuthorize("@commentChecker.isUserTheAuthor(#principal.getName(), #id) or hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteCommentById(@PathVariable Long id,
                                                  Principal principal) {
        commentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
