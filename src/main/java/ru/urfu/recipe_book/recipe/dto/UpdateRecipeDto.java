package ru.urfu.recipe_book.recipe.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.urfu.recipe_book.common.enums.DifficultyLevel;
import ru.urfu.recipe_book.common.enums.MealTime;
import ru.urfu.recipe_book.common.enums.PreferenceTag;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateRecipeDto {
    @NotBlank
    @Size(min = 3, max = 200, message = "Название должно быть от 3 до 200 символов")
    private String title;

    @Size(max = 5000, message = "Описание не может превышать 5000 символов")
    private String description;

    @Size(max = 500, message = "URL изображения не может превышать 500 символов")
    private String imageUrl;

    @NotNull(message = "Уровень сложности должен быть указан")
    private DifficultyLevel difficultyLevel;

    @NotNull(message = "Время подготовки должно быть указано")
    @Positive(message = "Время подготовки должно быть положительным числом")
    private Integer preparationTime;

    @NotNull(message = "Время готовки должно быть указано")
    @Positive(message = "Время готовки должно быть положительным числом")
    private Integer cookingTime;

    private Set<PreferenceTag> tags = new HashSet<>();

    @NotNull(message = "Время приёма пищи должно быть указано")
    private MealTime mealTime;
}
