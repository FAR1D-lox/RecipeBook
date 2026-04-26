package ru.urfu.recipe_book.reaction.service;

import ru.urfu.recipe_book.reaction.dto.ReactionStatsDto;
import ru.urfu.recipe_book.reaction.dto.ResponseReactionDto;

public interface ReactionService {

    ResponseReactionDto addReaction(Long recipeId, String email, boolean liked);

    void undoReaction(Long recipeId, String email);

    ResponseReactionDto getUserReaction(Long recipeId, String email);

    ReactionStatsDto getRecipeStats(Long recipeId, String email);

}