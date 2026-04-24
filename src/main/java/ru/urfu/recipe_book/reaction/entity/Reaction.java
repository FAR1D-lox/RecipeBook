package ru.urfu.recipe_book.reaction.entity;

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
@Table(name="likes",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_id", "recipe_id"})
        })
public class Reaction extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private Recipe recipe;

    private boolean liked;
}
