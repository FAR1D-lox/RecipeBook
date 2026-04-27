package ru.urfu.recipe_book.reaction.service;

import ru.urfu.recipe_book.reaction.dto.ReactionStatsDto;
import ru.urfu.recipe_book.reaction.dto.ResponseReactionDto;

public interface ReactionService {

    ResponseReactionDto addReaction(Long recipeId, Long userId, boolean liked);

    void undoReaction(Long recipeId, Long userId);

    ResponseReactionDto getUserReaction(Long recipeId, Long userId);

    ReactionStatsDto getRecipeStats(Long recipeId, Long userId);

}