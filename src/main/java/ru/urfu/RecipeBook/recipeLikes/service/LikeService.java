package ru.urfu.RecipeBook.recipeLikes.service;

public interface LikeService {

    void addLike(Long recipeId, Long userId);

    void addDislike(Long recipeId, Long userId);

    void undoLikeDislike(Long recipeId, Long userId);

    Long getLikesCount(Long recipeId);
}