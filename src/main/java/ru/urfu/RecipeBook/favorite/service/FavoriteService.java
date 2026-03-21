package ru.urfu.RecipeBook.favorite.service;

import ru.urfu.RecipeBook.favorite.entity.Favorite;

import java.util.List;

public interface FavoriteService {

    Favorite addFavorite(Long userId, Long recipeId);

    List<Favorite> getFavoriteByUser(Long userId);

    void deleteFavorite(Long userId, Long recipeId);
}
