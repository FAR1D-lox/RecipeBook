package ru.urfu.RecipeBook.recipeLikes.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.urfu.RecipeBook.recipe.entity.Recipe;
import ru.urfu.RecipeBook.recipe.repository.RecipeRepository;
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
    public void addLike(Long recipeId, Long userId) {
        addLikeDislike(recipeId, userId, true);

    }

    @Override
    public void addDislike(Long recipeId, Long userId) {
        addLikeDislike(recipeId, userId, false);
    }

    private void addLikeDislike(Long recipeId, Long userId, boolean likeDislike) {
        likeRepository.findByUserIdAndRecipeId(userId, recipeId)
                .ifPresentOrElse(
                        exitingLike -> {
                            exitingLike.setLike(likeDislike);
                            likeRepository.save(exitingLike);
                        },
                        () -> {
                            User user = userRepository.findById(userId)
                                    .orElseThrow(() -> new RuntimeException("user not found"));
                            Recipe recipe = recipeRepository.findById(recipeId)
                                    .orElseThrow(() -> new RuntimeException("recipe not found"));
                            Like like = new Like();
                            like.setUser(user);
                            like.setRecipe(recipe);
                            like.setLike(likeDislike);
                            likeRepository.save(like);
                        }
                );
    }

    @Override
    public void undoLikeDislike(Long recipeId, Long userId) {
        likeRepository.deleteByUserIdAndRecipeId(userId, recipeId);
    }

    @Override
    public Long getLikesCount(Long recipeId) {
        return likeRepository.countByRecipeId(recipeId);
    }
}
