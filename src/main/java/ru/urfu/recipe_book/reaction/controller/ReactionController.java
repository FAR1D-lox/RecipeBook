package ru.urfu.recipe_book.reaction.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.urfu.recipe_book.reaction.dto.CreateReactionDto;
import ru.urfu.recipe_book.reaction.dto.ReactionStatsDto;
import ru.urfu.recipe_book.reaction.dto.ResponseReactionDto;
import ru.urfu.recipe_book.reaction.service.ReactionService;
import ru.urfu.recipe_book.security.CustomUserDetails;

@RestController
@RequiredArgsConstructor
@RequestMapping("/recipes/{recipeId}/likes")
public class ReactionController {
    private final ReactionService reactionService;

    @PostMapping
    public ResponseReactionDto addReaction(
            @PathVariable Long recipeId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestBody CreateReactionDto createDto) {
        return reactionService.addReaction(recipeId, customUserDetails.getUserId(), createDto.isLiked());
    }

    @DeleteMapping
    void undoLikeDislike(
            @PathVariable Long recipeId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        reactionService.undoReaction(recipeId, customUserDetails.getUserId());
    }

    @GetMapping("/user")
    public ResponseReactionDto getUserReaction(
            @PathVariable Long recipeId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return reactionService.getUserReaction(recipeId, customUserDetails.getUserId());
    }

    @GetMapping("/stats")
    public ReactionStatsDto getRecipeStats(
            @PathVariable Long recipeId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return reactionService.getRecipeStats(recipeId, customUserDetails.getUserId());
    }
}
