package ru.urfu.recipe_book.comment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.urfu.recipe_book.comment.dto.CreateCommentDto;
import ru.urfu.recipe_book.comment.dto.ResponseCommentDto;
import ru.urfu.recipe_book.comment.service.CommentService;
import ru.urfu.recipe_book.security.CustomUserDetails;

import java.util.List;


@RequiredArgsConstructor
@RequestMapping(value = "/recipes/{recipeId}/comments")
@RestController
public class CommentController {
    private final CommentService commentService;

    @PostMapping()
    public ResponseCommentDto addComment(@PathVariable Long recipeId,
                                         @AuthenticationPrincipal CustomUserDetails customUserDetails,
                                         @RequestBody CreateCommentDto commentDto) {
        return commentService.addComment(recipeId, customUserDetails.getUserId(), commentDto);
    }

    @GetMapping
    public List<ResponseCommentDto> getCommentsByRecipe(@PathVariable Long recipeId) {
        return commentService.getCommentsByRecipe(recipeId);
    }

    @DeleteMapping("/{commentId}")
    public void deleteComment(@PathVariable Long commentId,
                              @AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable Long recipeId) {
        commentService.deleteComment(commentId, userDetails.getUserId());
    }
}
