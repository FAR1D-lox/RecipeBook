package ru.urfu.RecipeBook.recipe.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.urfu.RecipeBook.common.entities.BaseEntity;
import ru.urfu.RecipeBook.common.enums.DifficultyLevel;
import ru.urfu.RecipeBook.user.entity.User;
import java.time.OffsetDateTime;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="recipe",
        indexes = {
            @Index(name = "idx_recipe_title", columnList = "title")})
public class Recipe extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    private User author;

    private String title;

    private String description;

    private String imageUrl;

    @Enumerated(EnumType.STRING)
    private DifficultyLevel difficultyLevel;

    private Integer preparationTime;

    private Integer cookingTime;

    private Long viewsCount = 0L;

    private Long likesCount = 0L;

    private Long dislikesCount = 0L;

    private Long commentsCount = 0L;

}
