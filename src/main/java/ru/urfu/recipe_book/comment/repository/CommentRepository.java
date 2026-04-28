package ru.urfu.recipe_book.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.urfu.recipe_book.comment.entity.Comment;
import ru.urfu.recipe_book.user.entity.User;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByRecipeId(Long recipeId);

    void deleteByRecipeId(Long recipeId);

    void deleteByAuthor(User user);
}
