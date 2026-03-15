package ru.urfu.RecipeBook.recipe.service;

import ru.urfu.RecipeBook.recipe.entity.Recipe;
import java.util.List;


public interface RecipeService {

    Recipe createRecipe(Recipe recipe);

    List<Recipe> getAuthorRecipes(Long authorId);

    List <Recipe> searchRecipe(String title);

    List<Recipe> getAllRecipes();

    void deleteRecipe(Long recipeId);
}
