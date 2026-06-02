package ru.urfu.recipe_book.recipe.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Limit;
import org.springframework.stereotype.Service;
import ru.urfu.recipe_book.comment.repository.CommentRepository;
import ru.urfu.recipe_book.common.exception.ForbiddenOperationException;
import ru.urfu.recipe_book.common.exception.ResourceNotFoundException;
import ru.urfu.recipe_book.common.markdown.MarkdownService;
import ru.urfu.recipe_book.recipe.dto.CreateRecipeDto;
import ru.urfu.recipe_book.common.entities.CursorPageResponse;
import ru.urfu.recipe_book.recipe.dto.RecipeMapper;
import ru.urfu.recipe_book.recipe.dto.RecipeResponseDto;
import ru.urfu.recipe_book.recipe.dto.UpdateRecipeDto;
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
    private final RecipeMapper recipeMapper;
    private final MarkdownService markdownService;

    private RecipeResponseDto putHtml(Recipe recipe) {
        RecipeResponseDto dto = recipeMapper.toRecipeResponse(recipe);
        if (recipe.getDescription() != null) {
            String html = markdownService.render(recipe.getDescription());
            dto.setDescription(html);
        }
        return dto;
    }

    private List<RecipeResponseDto> putListHtml(List<Recipe> recipes) {
        return recipes.stream()
                .map(this::putHtml)
                .collect(Collectors.toList());
    }

    @Override
    public RecipeResponseDto createRecipe(Long authorId, CreateRecipeDto recipeDto) {
        Recipe recipe = recipeMapper.toEntity(recipeDto);
        recipe.setAuthor(userRepository.findById(authorId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found")));
        recipe.setCreatedAt(OffsetDateTime.now());
        recipeRepository.save(recipe);
        return putHtml(recipe);
    }

    @Override
    public List<RecipeResponseDto> getAuthorRecipes(Long authorId) {
        List<Recipe> authorRecipes = recipeRepository.findRecipesByAuthorId(authorId);
        return putListHtml(authorRecipes);
    }

    @Override
    public List<RecipeResponseDto> searchRecipes(String title) {
        List<Recipe> foundRecipes = recipeRepository.findByTitleContainingIgnoreCase(title);
        return putListHtml(foundRecipes);
    }

    public CursorPageResponse<RecipeResponseDto> getAllRecipes(Long cursor, int size) {
        if (size < 1 || size > 50) {
            throw new IllegalArgumentException("Size must be between 1 and 50");
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
                putListHtml(content),
                size,
                nextCursor,
                hasNext
        );
    }

    @Override
    public RecipeResponseDto findRecipeById(Long recipeId) {
        Recipe recipe = recipeRepository.findRecipeById(recipeId)
                .orElseThrow(() -> new ResourceNotFoundException("Recipe not found"));
        return putHtml(recipe);
    }

    @Transactional
    public void deleteRecipe(Long id, Long userId) {
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Recipe not found"));

        if (!recipe.getAuthor().getId().equals(userId)) {
            throw new ForbiddenOperationException("You can't delete foreign recipes");
        }

        commentRepository.deleteByRecipeId(id);

        recipeRepository.deleteById(id);
    }

    @Override
    @Transactional
    public RecipeResponseDto updateRecipe(Long recipeId, Long userId, UpdateRecipeDto updateDto) {
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new ResourceNotFoundException("Recipe not found"));

        if (!recipe.getAuthor().getId().equals(userId)) {
            throw new ForbiddenOperationException("You can't edit foreign recipes");
        }

        recipeMapper.updateEntity(updateDto, recipe);
        recipeRepository.save(recipe);
        return putHtml(recipe);
    }

}