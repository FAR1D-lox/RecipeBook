package ru.urfu.RecipeBook.recipeLikes.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.urfu.RecipeBook.recipe.entity.Recipe;
import ru.urfu.RecipeBook.recipe.repository.RecipeRepository;
import ru.urfu.RecipeBook.recipeLikes.dto.LikesStatsDto;
import ru.urfu.RecipeBook.recipeLikes.dto.ResponseLikeDto;
import ru.urfu.RecipeBook.recipeLikes.entity.Like;
import ru.urfu.RecipeBook.recipeLikes.repository.LikeRepository;
import ru.urfu.RecipeBook.recipeLikes.service.LikeService;
import ru.urfu.RecipeBook.user.entity.User;
import ru.urfu.RecipeBook.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {

    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final RecipeRepository recipeRepository;

    @Override
    public ResponseLikeDto addReaction(Long recipeId, Long userId, boolean isLike) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new RuntimeException("Recipe not found with id: " + recipeId));

        Like exitingLike = likeRepository.findByUserIdAndRecipeId(userId, recipeId).orElse(null);

        if (exitingLike != null) {
            exitingLike.setLike(isLike);
            Like save = likeRepository.save(exitingLike);
            return new ResponseLikeDto(
                    save.getRecipe().getId(),
                    save.getUser().getId(),
                    save.isLike(),
                    save.getUser().getUsername(),
                    save.getRecipe().getTitle());
        }
        else {
            Like like = new Like();
            like.setUser(user);
            like.setRecipe(recipe);
            like.setLike(isLike);

            Like save = likeRepository.save(like);
            return new ResponseLikeDto(
                    save.getRecipe().getId(),
                    save.getUser().getId(),
                    save.isLike(),
                    save.getUser().getUsername(),
                    save.getRecipe().getTitle());
        }
    }

    @Override
    public void undoReaction(Long recipeId, Long userId) {
        Like like = likeRepository.findByUserIdAndRecipeId(userId, recipeId)
                .orElseThrow(() -> new RuntimeException("Reaction not found"));
        likeRepository.delete(like);
    }

    @Override
    public ResponseLikeDto getUserReaction(Long recipeId, Long userId) {
        Like like = likeRepository.findByUserIdAndRecipeId(userId, recipeId)
                .orElseThrow(() -> new RuntimeException("Reaction not found"));
        return new ResponseLikeDto(
                like.getRecipe().getId(),
                like.getUser().getId(),
                like.isLike(),
                like.getUser().getUsername(),
                like.getRecipe().getTitle()
        );
    }

    @Override
    public LikesStatsDto getRecipeStats(Long recipeId, Long currentUserId) {
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new RuntimeException("Recipe not found with id: " + recipeId));

        Long likesCount = likeRepository.countByRecipeIdAndIsLike(recipeId, true);
        Long dislikesCount = likeRepository.countByRecipeIdAndIsLike(recipeId, false);

        Long currentUserReaction = null;

        if (currentUserId != null) {
            Long[] reactionHolder = new Long[1];
            likeRepository.findByUserIdAndRecipeId(currentUserId, recipeId)
                    .ifPresent(like -> reactionHolder[0] = like.isLike() ? 1L : 0L);
            currentUserReaction = reactionHolder[0];
        }

        return new LikesStatsDto(
                recipeId,
                likesCount,
                dislikesCount,
                currentUserReaction
        );
    }
}
