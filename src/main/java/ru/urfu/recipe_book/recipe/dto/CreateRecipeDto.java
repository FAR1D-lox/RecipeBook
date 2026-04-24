package ru.urfu.recipe_book.recipe.dto;

import lombok.*;
import ru.urfu.recipe_book.common.enums.DifficultyLevel;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateRecipeDto {
    private String title;

    private String description;

    private String imageUrl;

    private DifficultyLevel difficultyLevel;

    private Integer preparationTime;

    private Integer cookingTime;
}
