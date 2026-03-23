package ru.urfu.RecipeBook.recipeLikes.service;

import ru.urfu.RecipeBook.recipeLikes.dto.LikesStatsDto;
import ru.urfu.RecipeBook.recipeLikes.dto.ResponseLikeDto;

public interface LikeService {

    ResponseLikeDto addReaction(Long recipeId, Long userId, boolean isLike);

    void undoReaction(Long recipeId, Long userId);

    ResponseLikeDto getUserReaction(Long recipeId, Long userId);

    LikesStatsDto getRecipeStats(Long recipeId, Long currentUserId);

}