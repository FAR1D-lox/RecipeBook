package ru.urfu.recipe_book.reaction.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
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

    @Override
    public ResponseReactionDto addReaction(Long recipeId, Long userId, boolean liked) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new RuntimeException("Recipe not found with id: " + recipeId));

        Reaction exitingReaction = reactionRepository.findByUserIdAndRecipeId(userId, recipeId).orElse(null);

        if (exitingReaction != null) {
            exitingReaction.setLiked(liked);
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

            Reaction save = reactionRepository.save(reaction);
            return new ResponseReactionDto(
                    save.getRecipe().getId(),
                    save.getUser().getId(),
                    save.isLiked(),
                    save.getUser().getUsername(),
                    save.getRecipe().getTitle());
        }
    }

    @Override
    public void undoReaction(Long recipeId, Long userId) {
        Reaction reaction = reactionRepository.findByUserIdAndRecipeId(userId, recipeId)
                .orElseThrow(() -> new RuntimeException("Reaction not found"));
        reactionRepository.delete(reaction);
    }

    @Override
    public ResponseReactionDto getUserReaction(Long recipeId, Long userId) {
        Reaction reaction = reactionRepository.findByUserIdAndRecipeId(userId, recipeId)
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
    public ReactionStatsDto getRecipeStats(Long recipeId, Long currentUserId) {
        Long likesCount = reactionRepository.countByRecipeIdAndLiked(recipeId, true);
        Long dislikesCount = reactionRepository.countByRecipeIdAndLiked(recipeId, false);

        Long currentUserReaction = null;

        if (currentUserId != null) {
            Long[] reactionHolder = new Long[1];
            reactionRepository.findByUserIdAndRecipeId(currentUserId, recipeId)
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
