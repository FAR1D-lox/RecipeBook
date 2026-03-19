package ru.urfu.RecipeBook.recipe.controller;



import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.urfu.RecipeBook.recipe.dto.CreateRecipeDto;
import ru.urfu.RecipeBook.recipe.dto.RecipeResponseDto;
import ru.urfu.RecipeBook.recipe.service.impl.RecipeServiceImpl;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping(value = "/recipes")
@RestController()
public class RecipeController {

    private final RecipeServiceImpl recipeService;

    @PostMapping // позже переделать передачу authorId
    public RecipeResponseDto createRecipe(@RequestBody CreateRecipeDto createRecipeDto, Long authorId) {
        return recipeService.createRecipe(authorId, createRecipeDto);
    }

    @GetMapping()
    public List<RecipeResponseDto> getAllRecipes() {
        return recipeService.getAllRecipes();
    }

    @GetMapping(value = "/{id}")
    public RecipeResponseDto getRecipeById(@PathVariable Long id) {
        return recipeService.getRecipeById(id);
    }

    @GetMapping(value="/search")
    public List<RecipeResponseDto> searchRecipe(@RequestParam String query) {
        return recipeService.searchRecipe(query);
    }

    @DeleteMapping(value="/{id}")
    public void deleteRecipe(@PathVariable Long id) {
        recipeService.deleteRecipe(id);
    }

}
