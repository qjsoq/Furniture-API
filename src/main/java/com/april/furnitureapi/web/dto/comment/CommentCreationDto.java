package com.april.furnitureapi.web.dto.comment;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.Range;
@Data
public class CommentCreationDto {
    @Range(min = 0, max = 5)
    @Digits(integer = 1, fraction = 1)
    Double rating;
    @NotBlank(message = "Content cant be empty")
    @Size(message = "Content cant be greater than 2000 symbols, and less than 5 symbols",
            min = 5, max = 2000)
    String content;
}
