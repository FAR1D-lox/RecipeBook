package ru.urfu.recipe_book.favorite.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ResponseFavoriteDto {
    private Long favoriteId;
    private Long userId;
    private String username;
    private Long recipeId;
    private String recipeTitle;
    private String recipeImageUrl;
}
