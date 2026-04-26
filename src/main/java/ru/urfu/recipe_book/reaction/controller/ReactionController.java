package ru.urfu.recipe_book.reaction.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
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
            Authentication authentication,
            @RequestBody CreateReactionDto createDto) {
        String email = authentication.getName();
        return reactionService.addReaction(recipeId, email, createDto.isLiked());
    }

    @DeleteMapping
    void undoLikeDislike(
            @PathVariable Long recipeId,
            Authentication authentication) {
        String email = authentication.getName();
        reactionService.undoReaction(recipeId, email);
    }

    @GetMapping("/user")
    public ResponseReactionDto getUserReaction(
            @PathVariable Long recipeId,
            Authentication authentication) {
        String email = authentication.getName();
        return reactionService.getUserReaction(recipeId, email);
    }

    @GetMapping("/stats")
    public ReactionStatsDto getRecipeStats(
            @PathVariable Long recipeId,
            Authentication authentication) {
        String email = authentication.getName();
        return reactionService.getRecipeStats(recipeId, email);
    }
}
