package ru.urfu.recipe_book.recipe.service;


import ru.urfu.recipe_book.recipe.dto.CreateRecipeDto;
import ru.urfu.recipe_book.recipe.dto.CursorPageResponse;
import ru.urfu.recipe_book.recipe.dto.RecipeResponseDto;
import java.util.List;


public interface RecipeService {

    RecipeResponseDto createRecipe(Long authorId, CreateRecipeDto recipe);

    List<RecipeResponseDto> getAuthorRecipes(Long authorId);

    List <RecipeResponseDto> searchRecipes(String title);

    RecipeResponseDto findRecipeById(Long recipeId);

    CursorPageResponse<RecipeResponseDto> getAllRecipes(Long cursor, int size);

    void deleteRecipe(Long recipeId);
}
