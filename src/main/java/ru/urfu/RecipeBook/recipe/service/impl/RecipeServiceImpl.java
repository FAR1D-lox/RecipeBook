package ru.urfu.RecipeBook.recipe.service.impl;



import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.urfu.RecipeBook.recipe.dto.CreateRecipeDto;
import ru.urfu.RecipeBook.recipe.dto.RecipeResponseDto;
import ru.urfu.RecipeBook.recipe.entity.Recipe;
import ru.urfu.RecipeBook.recipe.repository.RecipeRepository;
import ru.urfu.RecipeBook.recipe.service.RecipeService;
import ru.urfu.RecipeBook.user.repository.UserRepository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository recipeRepository;
    private final UserRepository userRepository;

    @Override
    public RecipeResponseDto createRecipe(Long authorId, CreateRecipeDto recipeDto) {
        Recipe recipe = new Recipe();
        recipe.setAuthor(userRepository.getUserById(authorId));
        recipe.setCreatedAt(OffsetDateTime.now());
        recipe.setTitle(recipeDto.getTitle());
        recipe.setDescription(recipeDto.getDescription());
        recipe.setCookingTime(recipeDto.getCookingTime());
        recipe.setImageUrl(recipeDto.getImageUrl());
        recipe.setPreparationTime(recipeDto.getPreparationTime());
        recipe.setDifficultyLevel(recipeDto.getDifficultyLevel());

        Recipe saved = recipeRepository.save(recipe);

        RecipeResponseDto response = new RecipeResponseDto();
        response.setDescription(saved.getDescription());
        response.setDifficultyLevel(saved.getDifficultyLevel());
        response.setCookingTime(saved.getCookingTime());
        response.setPreparationTime(saved.getPreparationTime());
        response.setImageUrl(saved.getImageUrl());
        response.setAuthorId(saved.getAuthor().getId());
        response.setTitle(saved.getTitle());

        return response;
    }

    @Override
    public List<RecipeResponseDto> getAuthorRecipes(Long authorId) {
        List<Recipe> authorRecipes = recipeRepository.findRecipesByAuthorId(authorId);
        List<RecipeResponseDto> response = authorRecipes.stream()
                .map(recipe -> new RecipeResponseDto(
                        recipe.getId(),
                        recipe.getAuthor().getId(),
                        recipe.getTitle(),
                        recipe.getDescription(),
                        recipe.getPreparationTime(),
                        recipe.getCookingTime(),
                        recipe.getDifficultyLevel(),
                        recipe.getImageUrl(),
                        recipe.getLikesCount(),
                        recipe.getDislikesCount(),
                        recipe.getCommentsCount()
                ))
                .collect(Collectors.toList());

        return response;
    }

    @Override
    public List<RecipeResponseDto> searchRecipe(String title) {
        List<Recipe> foundRecipes = recipeRepository.findByTitleContaining(title);
        List<RecipeResponseDto> response = foundRecipes.stream()
                .map(recipe -> new RecipeResponseDto(
                        recipe.getId(),
                        recipe.getAuthor().getId(),
                        recipe.getTitle(),
                        recipe.getDescription(),
                        recipe.getPreparationTime(),
                        recipe.getCookingTime(),
                        recipe.getDifficultyLevel(),
                        recipe.getImageUrl(),
                        recipe.getLikesCount(),
                        recipe.getDislikesCount(),
                        recipe.getCommentsCount()

                ))
                .collect(Collectors.toList());

        return response;
    }

    @Override
    public List<RecipeResponseDto> getAllRecipes() {
        List<Recipe> allRecipes = recipeRepository.findAll();

        List<RecipeResponseDto> response = allRecipes.stream()
                .map(recipe -> new RecipeResponseDto(
                    recipe.getId(),
                    recipe.getAuthor().getId(),
                    recipe.getTitle(),
                    recipe.getDescription(),
                    recipe.getPreparationTime(),
                    recipe.getCookingTime(),
                    recipe.getDifficultyLevel(),
                    recipe.getImageUrl(),
                    recipe.getLikesCount(),
                    recipe.getDislikesCount(),
                    recipe.getCommentsCount()
                ))
                .collect(Collectors.toList());

        return response;
    }

    @Override
    public RecipeResponseDto getRecipeById(Long recipeId) {
        Recipe recipe = recipeRepository.getRecipeById(recipeId);

        RecipeResponseDto response = new RecipeResponseDto();
        response.setDescription(recipe.getDescription());
        response.setDifficultyLevel(recipe.getDifficultyLevel());
        response.setCookingTime(recipe.getCookingTime());
        response.setPreparationTime(recipe.getPreparationTime());
        response.setImageUrl(recipe.getImageUrl());
        response.setAuthorId(recipe.getAuthor().getId());
        response.setTitle(recipe.getTitle());

        return response;
    }

    @Override
    public void deleteRecipe(Long id) {
        if (recipeRepository.existsById(id)) {
            recipeRepository.deleteById(id);
        }
    }
}
