package com.april.furnitureapi.web.mapper;

import com.april.furnitureapi.domain.Comment;
import com.april.furnitureapi.web.dto.comment.CommentCreationDto;
import com.april.furnitureapi.web.dto.comment.CommentDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)

public interface CommentMapper {
    Comment creationDtoToComment(CommentCreationDto creationDto);

    CommentDto commentToDto(Comment comment);
}
