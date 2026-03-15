package ru.urfu.RecipeBook.recipe.service.impl;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.urfu.RecipeBook.recipe.entity.Recipe;
import ru.urfu.RecipeBook.recipe.repository.RecipeRepository;
import ru.urfu.RecipeBook.recipe.service.RecipeService;
import java.util.List;


@Service
@RequiredArgsConstructor
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository recipeRepository;


    @Override
    public Recipe createRecipe(Recipe recipe) {
        return recipeRepository.save(recipe);
    }

    @Override
    public List<Recipe> getAuthorRecipes(Long authorId) {
        return recipeRepository.findRecipesByAuthorId(authorId);
    }

    @Override
    public List<Recipe> searchRecipe(String title) {
        return recipeRepository.findByTitleContaining(title);
    }

    @Override
    public List<Recipe> getAllRecipes() {
        return recipeRepository.findAll();
    }

    @Override
    public void deleteRecipe(Long id) {
        if (recipeRepository.existsById(id)) {
            recipeRepository.deleteById(id);
        }
    }
}
