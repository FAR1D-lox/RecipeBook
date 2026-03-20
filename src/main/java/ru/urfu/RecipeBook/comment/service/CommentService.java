package ru.urfu.RecipeBook.comment.service;

import ru.urfu.RecipeBook.comment.dto.CreateCommentDto;
import ru.urfu.RecipeBook.comment.dto.ResponseCommentDto;


import java.util.List;

public interface CommentService {
    ResponseCommentDto addComment(Long recipeId, Long userId, CreateCommentDto commentDto);

    List<ResponseCommentDto> getCommentsByRecipe(Long recipeId);

    void deleteComment(Long commentId, Long userId);
}
