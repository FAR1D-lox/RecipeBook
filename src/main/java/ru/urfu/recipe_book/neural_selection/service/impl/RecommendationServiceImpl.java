package ru.urfu.recipe_book.neural_selection.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.urfu.recipe_book.neural_selection.entity.PreferenceVector;
import ru.urfu.recipe_book.neural_selection.entity.ScoredRecipe;
import ru.urfu.recipe_book.neural_selection.service.RecommendationService;
import ru.urfu.recipe_book.recipe.dto.RecipeMapper;
import ru.urfu.recipe_book.recipe.dto.RecipeResponseDto;
import ru.urfu.recipe_book.recipe.entity.Recipe;
import ru.urfu.recipe_book.recipe.repository.RecipeRepository;
import ru.urfu.recipe_book.user.entity.User;
import ru.urfu.recipe_book.user.repository.UserRepository;

import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class RecommendationServiceImpl implements RecommendationService {

    private final RecipeRepository recipeRepository;
    private final UserRepository userRepository;
    private final VectorService vectorService;
    private final RecipeMapper recipeMapper;
    private final Random random = new Random();
    private final static int COUNT_RECOMMENDATIONS = 10;

    @Override
    public List<RecipeResponseDto> getRecommendedRecipes(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        PreferenceVector userVector = vectorService.userToVector(user);
        List<Recipe> lastRecipes = recipeRepository.findTop1000ByOrderByCreatedAtDesc();

        List<ScoredRecipe> scoredRecipes = lastRecipes.stream()
                .map(recipe -> {
                    PreferenceVector recipeVector = vectorService.recipeToVector(recipe);
                    double similarity = vectorService.cosSimilarity(userVector, recipeVector);
                    double randomFactor = random.nextDouble();
                    double finalScore = similarity + randomFactor;

                    return new ScoredRecipe(recipe, finalScore);
                }).sorted((a, b) -> Double.compare(b.score(), a.score()))
                .limit(COUNT_RECOMMENDATIONS)
                .toList();

        return scoredRecipes
                .stream()
                .map(a -> recipeMapper.toRecipeResponse(a.recipe()))
                .toList();
    }
}
