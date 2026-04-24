package ru.urfu.recipe_book.recipe.dto;

import lombok.*;
import ru.urfu.recipe_book.common.enums.DifficultyLevel;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecipeResponseDto {
        private Long id;

        private Long authorId;

        private String title;

        private String description;

        private Integer preparationTime;

        private Integer cookingTime;

        private DifficultyLevel difficultyLevel;

        private String imageUrl;

        private Long likesCount;

        private Long dislikesCount;

        private Long commentsCount;

}
