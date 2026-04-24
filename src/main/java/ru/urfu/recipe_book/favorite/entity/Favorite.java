package ru.urfu.recipe_book.favorite.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.urfu.recipe_book.common.entities.BaseEntity;
import ru.urfu.recipe_book.recipe.entity.Recipe;
import ru.urfu.recipe_book.user.entity.User;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="favorite",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_id", "recipe_id"})
        })
public class Favorite extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id", nullable = false)
    private Recipe recipe;
}
