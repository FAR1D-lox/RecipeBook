package ru.urfu.RecipeBook.recipeLikes.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LikesStatsDto {
    private Long recipeId;
    private Long likesCount;
    private Long dislikesCount;
    private Long currentUserReaction;
}
