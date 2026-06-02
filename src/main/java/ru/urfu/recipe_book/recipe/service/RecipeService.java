package ru.urfu.recipe_book.recipe.service;


import ru.urfu.recipe_book.recipe.dto.CreateRecipeDto;
import ru.urfu.recipe_book.common.entities.CursorPageResponse;
import ru.urfu.recipe_book.recipe.dto.RecipeResponseDto;
import ru.urfu.recipe_book.recipe.dto.UpdateRecipeDto;
import java.util.List;


public interface RecipeService {

    RecipeResponseDto createRecipe(Long authorId, CreateRecipeDto recipe);

    List<RecipeResponseDto> getAuthorRecipes(Long authorId);

    List <RecipeResponseDto> searchRecipes(String title);

    RecipeResponseDto findRecipeById(Long recipeId);

    CursorPageResponse<RecipeResponseDto> getAllRecipes(Long cursor, int size);

    void deleteRecipe(Long recipeId, Long userId);

    RecipeResponseDto updateRecipe(Long recipeId, Long userId, UpdateRecipeDto updateDto);
}
