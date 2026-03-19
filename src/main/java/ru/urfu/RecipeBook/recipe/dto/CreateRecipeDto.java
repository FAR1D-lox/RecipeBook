package ru.urfu.RecipeBook.recipe.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.urfu.RecipeBook.common.enums.DifficultyLevel;

@Setter
@Getter
@AllArgsConstructor
public class CreateRecipeDto {
    private String title;

    private String description;

    private String imageUrl;

    private DifficultyLevel difficultyLevel;

    private Integer preparationTime;

    private Integer cookingTime;
}
