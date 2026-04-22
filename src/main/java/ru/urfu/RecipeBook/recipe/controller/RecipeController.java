package ru.urfu.RecipeBook.recipe.controller;



import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.urfu.RecipeBook.recipe.dto.CreateRecipeDto;
import ru.urfu.RecipeBook.recipe.dto.CursorPageResponse;
import ru.urfu.RecipeBook.recipe.dto.RecipeResponseDto;
import ru.urfu.RecipeBook.recipe.service.RecipeService;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping(value = "/recipes")
@RestController()
public class RecipeController {

    private final RecipeService recipeService;

    @PostMapping // позже переделать передачу authorId
    public RecipeResponseDto createRecipe(@RequestBody CreateRecipeDto createRecipeDto, @RequestParam Long authorId) {
        return recipeService.createRecipe(authorId, createRecipeDto);
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
    public void deleteRecipe(@PathVariable Long id) {
        recipeService.deleteRecipe(id);
    }

}
