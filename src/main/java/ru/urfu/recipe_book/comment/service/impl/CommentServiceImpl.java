package ru.urfu.RecipeBook.comment.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.urfu.RecipeBook.comment.dto.CommentMapper;
import ru.urfu.RecipeBook.comment.dto.CreateCommentDto;
import ru.urfu.RecipeBook.comment.dto.ResponseCommentDto;
import ru.urfu.RecipeBook.comment.entity.Comment;
import ru.urfu.RecipeBook.comment.repository.CommentRepository;
import ru.urfu.RecipeBook.comment.service.CommentService;
import ru.urfu.RecipeBook.recipe.entity.Recipe;
import ru.urfu.RecipeBook.recipe.repository.RecipeRepository;
import ru.urfu.RecipeBook.user.entity.User;
import ru.urfu.RecipeBook.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final RecipeRepository recipeRepository;
    private final CommentMapper commentMapper;

    @Override
    public ResponseCommentDto addComment(Long recipeId, Long userId, CreateCommentDto commentDto) {
        Comment comment = commentMapper.toEntity(commentDto);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("user not found"));

        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new RuntimeException("recipe not found"));

        comment.setAuthor(user);
        comment.setRecipe(recipe);

        commentRepository.save(comment);

        return commentMapper.toCommentResponse(comment);
    }

    @Override
    public List<ResponseCommentDto> getCommentsByRecipe(Long recipeId) {
        List<Comment> comments = commentRepository.findByRecipeId(recipeId);

        return commentMapper.toListCommentResponse(comments);
    }

    @Override
    public void deleteComment(Long commentId, Long userId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("comment not found"));

        if (!comment.getAuthor().getId().equals(userId)) {
            throw new RuntimeException("you can't delete foreign comment");
        }
        commentRepository.deleteById(commentId);
    }
}
