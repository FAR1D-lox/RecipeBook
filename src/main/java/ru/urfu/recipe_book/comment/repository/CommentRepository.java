package ru.urfu.recipe_book.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.urfu.recipe_book.comment.entity.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByRecipeId(Long recipeId);

    void deleteByRecipeId(Long recipeId);
}
