package ru.urfu.RecipeBook.favorite.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.urfu.RecipeBook.favorite.dto.CreateFavoriteDto;
import ru.urfu.RecipeBook.favorite.dto.ResponseFavoriteDto;
import ru.urfu.RecipeBook.favorite.service.FavoriteService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("users/{userId}/favorites")
public class FavoriteController {

    private final FavoriteService favoriteService;

    @PostMapping
    public ResponseFavoriteDto addFavorite(
            @PathVariable Long userId,
            @RequestBody CreateFavoriteDto createDto) {
        return favoriteService.addFavorite(userId, createDto.getRecipeId());
    }

    @GetMapping
    public List<ResponseFavoriteDto> getFavoriteByUser(
            @PathVariable Long userId) {
        return favoriteService.getFavoriteByUser(userId);
    }

    @DeleteMapping("/{favorite_id}")
    public void deleteFavorite(@PathVariable Long userId, @PathVariable Long favoriteId) {
        favoriteService.deleteFavorite(userId, favoriteId);
    }

}
