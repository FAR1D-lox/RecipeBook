package ru.urfu.RecipeBook.comment.service;

import ru.urfu.RecipeBook.comment.entity.Comment;

import java.util.List;

public interface CommentService {
    Comment addComment(Long userId, Long recipeId, String text);

    List<Comment> getCommentsByRecipe(Long recipeId);

    void deleteComment(Long commentId, Long userId);
}
