package ru.urfu.RecipeBook.favorite.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.urfu.RecipeBook.favorite.entity.Favorite;
import ru.urfu.RecipeBook.favorite.repository.FavoriteRepository;
import ru.urfu.RecipeBook.favorite.service.FavoriteService;
import ru.urfu.RecipeBook.recipe.entity.Recipe;
import ru.urfu.RecipeBook.recipe.repository.RecipeRepository;
import ru.urfu.RecipeBook.user.entity.User;
import ru.urfu.RecipeBook.user.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FavoriteServiceImpl implements FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final UserRepository userRepository;
    private final RecipeRepository recipeRepository;

    @Override
    public Favorite addFavorite(Long userId, Long recipeId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("user not found"));
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new RuntimeException("recipe not found"));

        Favorite favorite = new Favorite();
        favorite.setUser(user);
        favorite.setRecipe(recipe);

        return favoriteRepository.save(favorite);

    }

    @Override
    public List<Favorite> getFavoriteByUser(Long userId) {
        return favoriteRepository.findByUserId(userId);
    }

    @Override
    public void deleteFavorite(Long favoriteId, Long userId) {
        Favorite favorite = favoriteRepository.findById(favoriteId)
                .orElseThrow(() -> new RuntimeException("favorite not found"));

        if (!favorite.getUser().getId().equals(userId)) {
            throw new RuntimeException("you can't delete foreign favorite recipe");
        }

        favoriteRepository.delete(favorite);
    }
}
