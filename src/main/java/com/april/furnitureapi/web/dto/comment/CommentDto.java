package com.april.furnitureapi.web.dto.comment;

import lombok.Data;

import java.util.UUID;

@Data
public class CommentDto {
    Long id;
    String content;
    Double rating;
}
