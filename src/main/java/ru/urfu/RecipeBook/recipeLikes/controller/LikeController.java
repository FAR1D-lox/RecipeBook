package ru.urfu.RecipeBook.recipeLikes.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.urfu.RecipeBook.recipeLikes.dto.CreateLikeDto;
import ru.urfu.RecipeBook.recipeLikes.dto.LikesStatsDto;
import ru.urfu.RecipeBook.recipeLikes.dto.ResponseLikeDto;
import ru.urfu.RecipeBook.recipeLikes.service.LikeService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/recipes/{recipeId}/likes")
public class LikeController {
    private final LikeService likeService;

    @PostMapping
    public ResponseLikeDto addReaction(
            @PathVariable Long recipeId,
            @RequestParam Long userId,
            @RequestBody CreateLikeDto createDto) {
        return likeService.addReaction(recipeId, userId, createDto.isLike());
    }

    @DeleteMapping
    void undoLikeDislike(
            @PathVariable Long recipeId,
            @RequestParam Long userId) {
        likeService.undoReaction(recipeId, userId);
    }

    @GetMapping("user/{userId}")
    public ResponseLikeDto getUserReaction(
            @PathVariable Long recipeId,
            @PathVariable Long userId) {
        return likeService.getUserReaction(recipeId, userId);
    }

    @GetMapping("/stats")
    public LikesStatsDto getRecipeStats(
            @PathVariable Long recipeId,
            @RequestParam(required = false) Long userId) {
        return likeService.getRecipeStats(recipeId, userId);
    }
}
