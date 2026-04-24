package ru.urfu.recipe_book.reaction.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.urfu.recipe_book.reaction.dto.CreateReactionDto;
import ru.urfu.recipe_book.reaction.dto.ReactionStatsDto;
import ru.urfu.recipe_book.reaction.dto.ResponseReactionDto;
import ru.urfu.recipe_book.reaction.service.ReactionService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/recipes/{recipeId}/likes")
public class ReactionController {
    private final ReactionService reactionService;

    @PostMapping
    public ResponseReactionDto addReaction(
            @PathVariable Long recipeId,
            @RequestParam Long userId,
            @RequestBody CreateReactionDto createDto) {
        return reactionService.addReaction(recipeId, userId, createDto.isLiked());
    }

    @DeleteMapping
    void undoLikeDislike(
            @PathVariable Long recipeId,
            @RequestParam Long userId) {
        reactionService.undoReaction(recipeId, userId);
    }

    @GetMapping("user/{userId}")
    public ResponseReactionDto getUserReaction(
            @PathVariable Long recipeId,
            @PathVariable Long userId) {
        return reactionService.getUserReaction(recipeId, userId);
    }

    @GetMapping("/stats")
    public ReactionStatsDto getRecipeStats(
            @PathVariable Long recipeId,
            @RequestParam(required = false) Long userId) {
        return reactionService.getRecipeStats(recipeId, userId);
    }
}
