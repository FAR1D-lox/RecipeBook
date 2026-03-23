package ru.urfu.RecipeBook.recipeLikes.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ResponseLikeDto {
    private Long recipeId;
    private Long userId;
    private boolean isLike;
    private String username;
    private String recipeTitle;
}
