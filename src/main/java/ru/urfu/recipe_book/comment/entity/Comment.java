package ru.urfu.recipe_book.comment.entity;

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
@Table(name="comment")
public class Comment extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    private User author;

    @ManyToOne(fetch = FetchType.LAZY)
    private Recipe recipe;

    @Column(columnDefinition = "TEXT")
    private String text;

}
