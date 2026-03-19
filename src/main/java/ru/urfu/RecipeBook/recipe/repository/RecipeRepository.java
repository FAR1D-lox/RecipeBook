package ru.urfu.RecipeBook.recipe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.urfu.RecipeBook.recipe.entity.Recipe;

import java.util.List;


public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    List<Recipe> findRecipesByAuthorId(Long authorId);
    Recipe getRecipeById(Long recipeId);
    List<Recipe> findByTitleContaining(String title);
}
