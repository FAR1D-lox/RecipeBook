package ru.urfu.RecipeBook.recipe.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Limit;
import org.springframework.stereotype.Service;
import ru.urfu.RecipeBook.comment.repository.CommentRepository;
import ru.urfu.RecipeBook.recipe.dto.CreateRecipeDto;
import ru.urfu.RecipeBook.recipe.dto.CursorPageResponse;
import ru.urfu.RecipeBook.recipe.dto.RecipeMapper;
import ru.urfu.RecipeBook.recipe.dto.RecipeResponseDto;
import ru.urfu.RecipeBook.recipe.entity.Recipe;
import ru.urfu.RecipeBook.recipe.repository.RecipeRepository;
import ru.urfu.RecipeBook.recipe.service.RecipeService;
import ru.urfu.RecipeBook.user.repository.UserRepository;
import java.util.List;


@Service
@RequiredArgsConstructor
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository recipeRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final RecipeMapper recipeMapper;

    @Override
    public RecipeResponseDto createRecipe(Long authorId, CreateRecipeDto recipeDto) {
        Recipe recipe = recipeMapper.toEntity(recipeDto);
        recipe.setAuthor(userRepository.findById(authorId).orElseThrow(() -> new RuntimeException("user not found")));
        recipeRepository.save(recipe);

        return recipeMapper.toRecipeResponse(recipe);
    }

    @Override
    public List<RecipeResponseDto> getAuthorRecipes(Long authorId) {
        List<Recipe> authorRecipes = recipeRepository.findRecipesByAuthorId(authorId);

        return recipeMapper.toRecipeResponseList(authorRecipes);
    }

    @Override
    public List<RecipeResponseDto> searchRecipes(String title) {
        List<Recipe> foundRecipes = recipeRepository.findByTitleContainingIgnoreCase(title);

        return recipeMapper.toRecipeResponseList(foundRecipes);
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
                recipeMapper.toRecipeResponseList(content),
                size,
                nextCursor,
                hasNext
        );
    }

    @Override
    public RecipeResponseDto findRecipeById(Long recipeId) {
        Recipe recipe = recipeRepository.findRecipeById(recipeId);

        return recipeMapper.toRecipeResponse(recipe);
    }

    @Override
    @Transactional
    public void deleteRecipe(Long id) {
        if (recipeRepository.existsById(id)) {
            commentRepository.deleteByRecipeId(id);
            recipeRepository.deleteById(id);
        }
    }
}
