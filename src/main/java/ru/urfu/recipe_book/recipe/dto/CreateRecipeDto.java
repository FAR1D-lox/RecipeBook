package ru.urfu.recipe_book.recipe.dto;

import lombok.*;
import ru.urfu.recipe_book.common.enums.DifficultyLevel;
import ru.urfu.recipe_book.common.enums.MealTime;
import ru.urfu.recipe_book.common.enums.PreferenceTag;

import java.util.HashSet;
import java.util.Set;

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

    private Set<PreferenceTag> tags = new HashSet<>();
    private MealTime mealTime;
}
