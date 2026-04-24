package ru.urfu.recipe_book.reaction.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ResponseReactionDto {
    private Long recipeId;
    private Long userId;
    private boolean liked;
    private String username;
    private String recipeTitle;
}
