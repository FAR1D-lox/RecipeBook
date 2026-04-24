package ru.urfu.recipe_book.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class CreateCommentDto {
    private String text;
}
