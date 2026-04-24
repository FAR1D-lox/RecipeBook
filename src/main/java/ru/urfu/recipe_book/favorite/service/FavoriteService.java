package ru.urfu.recipe_book.favorite.service;

import ru.urfu.recipe_book.favorite.dto.ResponseFavoriteDto;

import java.util.List;

public interface FavoriteService {

    ResponseFavoriteDto addFavorite(Long userId, Long recipeId);

    List<ResponseFavoriteDto> getFavoriteByUser(Long userId);

    void deleteFavorite(Long userId, Long recipeId);
}
