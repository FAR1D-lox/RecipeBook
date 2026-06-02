package ru.urfu.recipe_book.reaction.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.urfu.recipe_book.common.exception.ResourceNotFoundException;
import ru.urfu.recipe_book.recipe.entity.Recipe;
import ru.urfu.recipe_book.recipe.repository.RecipeRepository;
import ru.urfu.recipe_book.reaction.dto.ReactionStatsDto;
import ru.urfu.recipe_book.reaction.dto.ResponseReactionDto;
import ru.urfu.recipe_book.reaction.entity.Reaction;
import ru.urfu.recipe_book.reaction.repository.ReactionRepository;
import ru.urfu.recipe_book.reaction.service.ReactionService;
import ru.urfu.recipe_book.user.entity.User;
import ru.urfu.recipe_book.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class ReactionServiceImpl implements ReactionService {

    private final ReactionRepository reactionRepository;
    private final UserRepository userRepository;
    private final RecipeRepository recipeRepository;

    @Transactional
    @Override
    public ResponseReactionDto addReaction(Long recipeId, Long userId, boolean liked) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new RuntimeException("Recipe not found with id: " + recipeId));

        Reaction exitingReaction = reactionRepository.findByUserAndRecipeId(user, recipeId).orElse(null);

        if (exitingReaction != null) {
            if (exitingReaction.isLiked() != liked) {
                exitingReaction.setLiked(liked);
                if (liked) {
                    recipeRepository.incrementLikesCount(recipeId);
                    recipeRepository.decrementDislikesCount(recipeId);
                }
                else {
                    recipeRepository.incrementDislikesCount(recipeId);
                    recipeRepository.decrementLikesCount(recipeId);
                }
            }
            Reaction save = reactionRepository.save(exitingReaction);
            return new ResponseReactionDto(
                    save.getRecipe().getId(),
                    save.getUser().getId(),
                    save.isLiked(),
                    save.getUser().getUsername(),
                    save.getRecipe().getTitle());
        }
        else {
            Reaction reaction = new Reaction();
            reaction.setUser(user);
            reaction.setRecipe(recipe);
            reaction.setLiked(liked);

            if (liked)
                recipeRepository.incrementLikesCount(recipeId);
            else
                recipeRepository.incrementDislikesCount(recipeId);

            Reaction save = reactionRepository.save(reaction);
            return new ResponseReactionDto(
                    save.getRecipe().getId(),
                    save.getUser().getId(),
                    save.isLiked(),
                    save.getUser().getUsername(),
                    save.getRecipe().getTitle());
        }
    }

    @Transactional
    @Override
    public void undoReaction(Long recipeId, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));
        Reaction reaction = reactionRepository.findByUserAndRecipeId(user, recipeId)
                .orElseThrow(() -> new RuntimeException("Reaction not found"));
        Recipe recipe = recipeRepository.findRecipeById(recipeId)
                .orElseThrow(() -> new RuntimeException("Recipe not found: " + recipeId));
        if (reaction.isLiked())
            recipeRepository.decrementLikesCount(recipeId);
        else
            recipeRepository.decrementDislikesCount(recipeId);
        reactionRepository.delete(reaction);
    }

    @Override
    public ResponseReactionDto getUserReaction(Long recipeId, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));
        Reaction reaction = reactionRepository.findByUserAndRecipeId(user, recipeId)
                .orElseThrow(() -> new RuntimeException("Reaction not found"));
        return new ResponseReactionDto(
                reaction.getRecipe().getId(),
                reaction.getUser().getId(),
                reaction.isLiked(),
                reaction.getUser().getUsername(),
                reaction.getRecipe().getTitle()
        );
    }

    @Override
    public ReactionStatsDto getRecipeStats(Long recipeId, Long userId) {
        Recipe recipe = recipeRepository.findRecipeById(recipeId)
                .orElseThrow(() -> new ResourceNotFoundException("Recipe not found"));
        Long likesCount = recipe.getLikesCount();
        Long dislikesCount = recipe.getDislikesCount();

        Long currentUserReaction = null;
        User user = null;
        if (userId != null) {
            user = userRepository.findById(userId).orElse(null);
        }

        if (user != null) {
            Long[] reactionHolder = new Long[1];
            reactionRepository.findByUserAndRecipeId(user, recipeId)
                    .ifPresent(reaction -> reactionHolder[0] = reaction.isLiked() ? 1L : 0L);
            currentUserReaction = reactionHolder[0];
        }

        return new ReactionStatsDto(
                recipeId,
                likesCount,
                dislikesCount,
                currentUserReaction
        );
    }
}
