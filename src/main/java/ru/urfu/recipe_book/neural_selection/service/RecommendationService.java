package ru.urfu.recipe_book.neural_selection.service;

import ru.urfu.recipe_book.recipe.dto.RecipeResponseDto;

import java.util.List;

public interface RecommendationService {
    List<RecipeResponseDto> getRecommendedRecipes(Long userId);
}
