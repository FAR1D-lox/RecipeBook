package ru.urfu.recipe_book.recipe.controller;



import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.urfu.recipe_book.common.entities.CursorPageResponse;
import ru.urfu.recipe_book.recipe.dto.CreateRecipeDto;
import ru.urfu.recipe_book.recipe.dto.RecipeResponseDto;
import ru.urfu.recipe_book.recipe.dto.UpdateRecipeDto;
import ru.urfu.recipe_book.recipe.service.RecipeService;
import ru.urfu.recipe_book.security.CustomUserDetails;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping(value = "/recipes")
@RestController()
public class RecipeController {

    private final RecipeService recipeService;

    @PostMapping // позже переделать передачу authorId
    public ResponseEntity<RecipeResponseDto> createRecipe(@RequestBody CreateRecipeDto createRecipeDto,
                                                            @AuthenticationPrincipal CustomUserDetails userDetails) {
        RecipeResponseDto recipe = recipeService.createRecipe(userDetails.getUserId(), createRecipeDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(recipe);
    }

    @GetMapping()
    public ResponseEntity<CursorPageResponse<RecipeResponseDto>> getAllRecipes(
            @RequestParam(required = false) Long cursor,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(recipeService.getAllRecipes(cursor, size));
    }

    @GetMapping("/author/{authorId}")
    public ResponseEntity<List<RecipeResponseDto>> getAuthorRecipes(@PathVariable Long authorId) {
        return ResponseEntity.ok(recipeService.getAuthorRecipes(authorId));
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<RecipeResponseDto> getRecipeById(@PathVariable Long id) {
        return ResponseEntity.ok(recipeService.findRecipeById(id));
    }

    @GetMapping(value = "/search")
    public ResponseEntity<List<RecipeResponseDto>> searchRecipes(@RequestParam String query) {
        return ResponseEntity.ok(recipeService.searchRecipes(query));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RecipeResponseDto> updateRecipe(
            @PathVariable Long id,
            @RequestBody UpdateRecipeDto updateDto,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(recipeService.updateRecipe(id, userDetails.getUserId(), updateDto));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteRecipe(@PathVariable Long id,
                                             @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        recipeService.deleteRecipe(id, customUserDetails.getUserId());
        return ResponseEntity.noContent().build();
    }

}
