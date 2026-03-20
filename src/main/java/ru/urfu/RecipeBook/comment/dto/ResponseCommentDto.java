package ru.urfu.RecipeBook.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ResponseCommentDto {
    private Long commentId;

    private Long authorId;

    private String authorUsername;

    private String text;

}
