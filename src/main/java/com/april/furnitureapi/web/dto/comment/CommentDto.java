package com.april.furnitureapi.web.dto.comment;

import lombok.Data;

@Data
public class CommentDto {
    Long id;
    String content;
    Double rating;
}
