package ru.urfu.recipe_book.recipe.controller;



import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.urfu.recipe_book.recipe.dto.CreateRecipeDto;
import ru.urfu.recipe_book.common.entities.CursorPageResponse;
import ru.urfu.recipe_book.recipe.dto.RecipeResponseDto;
import ru.urfu.recipe_book.recipe.service.RecipeService;
import ru.urfu.recipe_book.security.CustomUserDetails;
import ru.urfu.recipe_book.user.entity.User;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping(value = "/recipes")
@RestController()
public class RecipeController {

    private final RecipeService recipeService;

    @PostMapping // позже переделать передачу authorId
    public RecipeResponseDto createRecipe(@RequestBody CreateRecipeDto createRecipeDto,
                                          @AuthenticationPrincipal CustomUserDetails userDetails) {
        return recipeService.createRecipe(userDetails.getUserId(), createRecipeDto);
    }

    @GetMapping()
    public CursorPageResponse<RecipeResponseDto> getAllRecipes(
            @RequestParam(required = false) Long cursor,
            @RequestParam(defaultValue = "10") int size)
    {
        return recipeService.getAllRecipes(cursor, size);
    }

    @GetMapping("/author/{authorId}")
    public List<RecipeResponseDto> getAuthorRecipes(@PathVariable Long authorId) {
        return recipeService.getAuthorRecipes(authorId);
    }

    @GetMapping(value = "/{id}")
    public RecipeResponseDto getRecipeById(@PathVariable Long id) {
        return recipeService.findRecipeById(id);
    }

    @GetMapping(value="/search")
    public List<RecipeResponseDto> searchRecipes(@RequestParam String query) {
        return recipeService.searchRecipes(query);
    }

    @DeleteMapping(value="/{id}")
    public void deleteRecipe(@PathVariable Long id,
                             @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        recipeService.deleteRecipe(customUserDetails.getUserId(), id);
    }

}
