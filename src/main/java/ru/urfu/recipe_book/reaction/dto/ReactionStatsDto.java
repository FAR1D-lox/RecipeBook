package ru.urfu.recipe_book.reaction.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ReactionStatsDto {
    private Long recipeId;
    private Long likesCount;
    private Long dislikesCount;
    private Long currentUserReaction;
}
