package ru.urfu.recipe_book.comment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.urfu.recipe_book.comment.dto.CreateCommentDto;
import ru.urfu.recipe_book.comment.dto.ResponseCommentDto;
import ru.urfu.recipe_book.comment.service.CommentService;

import java.util.List;


@RequiredArgsConstructor
@RequestMapping(value = "/recipes/{recipeId}/comments")
@RestController
public class CommentController {
    private final CommentService commentService;

    @PostMapping()
    public ResponseCommentDto addComment(@PathVariable Long recipeId,
             @RequestParam Long userId, @RequestBody CreateCommentDto commentDto) {
        return commentService.addComment(recipeId, userId, commentDto);
    }

    @GetMapping
    public List<ResponseCommentDto> getCommentsByRecipe(@PathVariable Long recipeId) {
        return commentService.getCommentsByRecipe(recipeId);
    }

    @DeleteMapping("/{commentId}")
    public void deleteComment(@PathVariable Long commentId, @RequestParam Long userId, @PathVariable Long recipeId) {
        commentService.deleteComment(commentId, userId);
    }
}
