package ru.urfu.recipe_book.recipe.service.impl;



import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Limit;
import org.springframework.stereotype.Service;
import ru.urfu.recipe_book.comment.repository.CommentRepository;
import ru.urfu.recipe_book.recipe.dto.CreateRecipeDto;
import ru.urfu.recipe_book.recipe.dto.CursorPageResponse;
import ru.urfu.recipe_book.recipe.dto.RecipeResponseDto;
import ru.urfu.recipe_book.recipe.entity.Recipe;
import ru.urfu.recipe_book.recipe.repository.RecipeRepository;
import ru.urfu.recipe_book.recipe.service.RecipeService;
import ru.urfu.recipe_book.user.repository.UserRepository;


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
        List<Recipe> foundRecipes = recipeRepository.findByTitleContainingIgnoreCase(title);
        return mapRecipeListEntitiesToDto(foundRecipes);
    }

    public CursorPageResponse<RecipeResponseDto> getAllRecipes(Long cursor, int size) {
        if (size < 1 || size > 50) {
            throw new IllegalArgumentException("Size must be between 1 and 100");
        }

        if (cursor != null && cursor < 0) {
            throw new IllegalArgumentException("Cursor must be positive");
        }

        List<Recipe> recipes = recipeRepository.fetchNextPage(cursor, Limit.of(size + 1));

        boolean hasNext = recipes.size() > size;

        List<Recipe> content = hasNext
                ? recipes.subList(0, size)
                : recipes;

        Long nextCursor = hasNext
                ? content.get(size - 1).getId()
                : null;

        return new CursorPageResponse<>(
                mapRecipeListEntitiesToDto(content),
                size,
                nextCursor,
                hasNext
        );
    }

    @Override
    public RecipeResponseDto findRecipeById(Long recipeId) {
        Recipe recipe = recipeRepository.findRecipeById(recipeId);

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
