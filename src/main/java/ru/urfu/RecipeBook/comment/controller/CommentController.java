package ru.urfu.RecipeBook.comment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.urfu.RecipeBook.comment.dto.CreateCommentDto;
import ru.urfu.RecipeBook.comment.dto.ResponseCommentDto;
import ru.urfu.RecipeBook.comment.service.CommentService;

import java.util.List;


@RequiredArgsConstructor
@RequestMapping(value = "/recipe/{recipeId}/comments")
@RestController
public class CommentController {
    private final CommentService commentService;

    @PostMapping()
    public ResponseCommentDto addComment(@PathVariable Long recipeId,
             Long userId, @RequestBody CreateCommentDto commentDto) {
        return commentService.addComment(recipeId, userId, commentDto);
    }

    @GetMapping
    public List<ResponseCommentDto> getCommentsByRecipe(@PathVariable Long recipeId) {
        return commentService.getCommentsByRecipe(recipeId);
    }

    @DeleteMapping("/{commentId}")
    public void deleteComment(@PathVariable Long commentId, Long userId) {
        commentService.deleteComment(commentId, userId);
    }
}
