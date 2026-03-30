package ru.urfu.RecipeBook.recipe.service.impl;



import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.urfu.RecipeBook.comment.repository.CommentRepository;
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
    private final CommentRepository commentRepository;

    @Override
    public RecipeResponseDto createRecipe(Long authorId, CreateRecipeDto recipeDto) {
        Recipe recipe = new Recipe();
        recipe.setAuthor(userRepository.findById(authorId).orElseThrow(() -> new RuntimeException("user not found")));
        recipe.setCreatedAt(OffsetDateTime.now());
        recipe.setTitle(recipeDto.getTitle());
        recipe.setDescription(recipeDto.getDescription());
        recipe.setCookingTime(recipeDto.getCookingTime());
        recipe.setImageUrl(recipeDto.getImageUrl());
        recipe.setPreparationTime(recipeDto.getPreparationTime());
        recipe.setDifficultyLevel(recipeDto.getDifficultyLevel());

        Recipe saved = recipeRepository.save(recipe);

        RecipeResponseDto response = mapRecipeEntityToDto(saved);

        return response;
    }

    @Override
    public List<RecipeResponseDto> getAuthorRecipes(Long authorId) {
        List<Recipe> authorRecipes = recipeRepository.findRecipesByAuthorId(authorId);
        return mapRecipeListEntitiesToDto(authorRecipes);
    }

    @Override
    public List<RecipeResponseDto> searchRecipes(String title) {
        List<Recipe> foundRecipes = recipeRepository.findByTitleContaining(title);
        return mapRecipeListEntitiesToDto(foundRecipes);
    }

    @Override
    public List<RecipeResponseDto> getAllRecipes() {
        List<Recipe> allRecipes = recipeRepository.findAll();

        return mapRecipeListEntitiesToDto(allRecipes);
    }

    @Override
    public RecipeResponseDto getRecipeById(Long recipeId) {
        Recipe recipe = recipeRepository.getRecipeById(recipeId);

        RecipeResponseDto response = mapRecipeEntityToDto(recipe);

        return response;
    }

    @Override
    @Transactional
    public void deleteRecipe(Long id) {
        if (recipeRepository.existsById(id)) {
            commentRepository.deleteByRecipeId(id);
            recipeRepository.deleteById(id);
        }
    }

    private List<RecipeResponseDto> mapRecipeListEntitiesToDto(List<Recipe> RecipeEntities) {
        return RecipeEntities.stream()
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
    }

    private RecipeResponseDto mapRecipeEntityToDto(Recipe RecipeEntity) {
        RecipeResponseDto response = new RecipeResponseDto();
        response.setDescription(RecipeEntity.getDescription());
        response.setDifficultyLevel(RecipeEntity.getDifficultyLevel());
        response.setCookingTime(RecipeEntity.getCookingTime());
        response.setPreparationTime(RecipeEntity.getPreparationTime());
        response.setImageUrl(RecipeEntity.getImageUrl());
        response.setAuthorId(RecipeEntity.getAuthor().getId());
        response.setTitle(RecipeEntity.getTitle());

        return response;
    }
}
