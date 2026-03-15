package ru.urfu.RecipeBook.comment.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.urfu.RecipeBook.comment.entity.Comment;
import ru.urfu.RecipeBook.comment.repository.CommentRepository;
import ru.urfu.RecipeBook.comment.service.CommentService;
import ru.urfu.RecipeBook.recipe.entity.Recipe;
import ru.urfu.RecipeBook.recipe.repository.RecipeRepository;
import ru.urfu.RecipeBook.user.entity.User;
import ru.urfu.RecipeBook.user.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final RecipeRepository recipeRepository;

    @Override
    public Comment addComment(Long userId, Long recipeId, String text) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("user not found"));
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new RuntimeException("recipe not found"));

        Comment comment = new Comment();
        comment.setAuthor(user);
        comment.setRecipe(recipe);
        comment.setText(text);

        return commentRepository.save(comment);
    }

    @Override
    public List<Comment> getCommentsByRecipe(Long recipeId) {
        return commentRepository.findByRecipeId(recipeId);
    }

    @Override
    public void deleteComment(Long commentId, Long userId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("comment not found"));

        if (!comment.getAuthor().getId().equals(userId)) {
            throw new RuntimeException("you can't delete foreign comment");
        }


    }
}
