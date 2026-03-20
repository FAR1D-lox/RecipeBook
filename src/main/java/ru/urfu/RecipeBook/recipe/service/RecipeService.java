package ru.urfu.RecipeBook.recipe.service;


import ru.urfu.RecipeBook.recipe.dto.CreateRecipeDto;
import ru.urfu.RecipeBook.recipe.dto.RecipeResponseDto;
import java.util.List;


public interface RecipeService {

    RecipeResponseDto createRecipe(Long authorId, CreateRecipeDto recipe);

    List<RecipeResponseDto> getAuthorRecipes(Long authorId);

    List <RecipeResponseDto> searchRecipe(String title);

    RecipeResponseDto getRecipeById(Long recipeId);

    List<RecipeResponseDto> getAllRecipes();

    void deleteRecipe(Long recipeId);
}
