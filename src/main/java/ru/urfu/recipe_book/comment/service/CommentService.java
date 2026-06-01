package ru.urfu.recipe_book.comment.service;

import ru.urfu.recipe_book.comment.dto.CreateCommentDto;
import ru.urfu.recipe_book.comment.dto.ResponseCommentDto;


import java.util.List;

public interface CommentService {
    ResponseCommentDto addComment(Long recipeId, Long userId, CreateCommentDto commentDto);

    List<ResponseCommentDto> getCommentsByRecipe(Long recipeId);

    void deleteComment(Long commentId, Long userId, Long recipeId);
}
