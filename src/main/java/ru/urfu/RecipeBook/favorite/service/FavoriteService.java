package ru.urfu.RecipeBook.favorite.service;

import ru.urfu.RecipeBook.favorite.dto.ResponseFavoriteDto;
import ru.urfu.RecipeBook.favorite.entity.Favorite;

import java.util.List;

public interface FavoriteService {

    ResponseFavoriteDto addFavorite(Long userId, Long recipeId);

    List<ResponseFavoriteDto> getFavoriteByUser(Long userId);

    void deleteFavorite(Long userId, Long recipeId);
}
