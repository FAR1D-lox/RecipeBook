package ru.urfu.RecipeBook.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.urfu.RecipeBook.comment.entity.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByRecipeId(Long recipeId);
}
