package ru.urfu.recipe_book.favorite.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.urfu.recipe_book.favorite.dto.CreateFavoriteDto;
import ru.urfu.recipe_book.favorite.dto.ResponseFavoriteDto;
import ru.urfu.recipe_book.favorite.service.FavoriteService;
import ru.urfu.recipe_book.security.CustomUserDetails;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("users/favorites")
public class FavoriteController {

    private final FavoriteService favoriteService;

    @PostMapping
    public ResponseFavoriteDto addFavorite(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestBody CreateFavoriteDto createDto) {
        return favoriteService.addFavorite(customUserDetails.getUserId(), createDto.getRecipeId());
    }

    @GetMapping
    public List<ResponseFavoriteDto> getFavoriteByUser(
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return favoriteService.getFavoriteByUser(customUserDetails.getUserId());
    }

    @DeleteMapping("/{favoriteId}")
    public void deleteFavorite(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                               @PathVariable Long favoriteId) {
        favoriteService.deleteFavorite(customUserDetails.getUserId(), favoriteId);
    }
}
