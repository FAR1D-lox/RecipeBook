package ru.urfu.recipe_book.neural_selection.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.urfu.recipe_book.neural_selection.service.RecommendationService;
import ru.urfu.recipe_book.recipe.dto.RecipeResponseDto;
import ru.urfu.recipe_book.security.CustomUserDetails;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/recommendations")
public class RecommendationController {

    private final RecommendationService recommendationService;

    @GetMapping
    public List<RecipeResponseDto> getRecommendations(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return recommendationService.getRecommendedRecipes(userDetails.getUserId());
    }

}
