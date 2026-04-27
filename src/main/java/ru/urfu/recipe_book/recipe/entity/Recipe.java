package ru.urfu.recipe_book.recipe.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.urfu.recipe_book.common.entities.BaseEntity;
import ru.urfu.recipe_book.common.enums.DifficultyLevel;
import ru.urfu.recipe_book.user.entity.User;


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

    @Column(columnDefinition="TEXT")
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
