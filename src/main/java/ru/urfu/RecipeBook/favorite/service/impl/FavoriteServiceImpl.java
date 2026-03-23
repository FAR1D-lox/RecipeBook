package ru.urfu.RecipeBook.favorite.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.urfu.RecipeBook.favorite.dto.ResponseFavoriteDto;
import ru.urfu.RecipeBook.favorite.entity.Favorite;
import ru.urfu.RecipeBook.favorite.repository.FavoriteRepository;
import ru.urfu.RecipeBook.favorite.service.FavoriteService;
import ru.urfu.RecipeBook.recipe.entity.Recipe;
import ru.urfu.RecipeBook.recipe.repository.RecipeRepository;
import ru.urfu.RecipeBook.user.entity.User;
import ru.urfu.RecipeBook.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FavoriteServiceImpl implements FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final UserRepository userRepository;
    private final RecipeRepository recipeRepository;

    @Override
    public ResponseFavoriteDto addFavorite(Long userId, Long recipeId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("user not found"));
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new RuntimeException("recipe not found"));

        if (favoriteRepository.existsByUserIdAndRecipeId(userId, recipeId)) {
            throw new RuntimeException("This recipe has already favorite");
        }

        Favorite favorite = new Favorite();
        favorite.setUser(user);
        favorite.setRecipe(recipe);

        Favorite saved = favoriteRepository.save(favorite);

        return new ResponseFavoriteDto(
                saved.getId(),
                saved.getUser().getId(),
                saved.getUser().getUsername(),
                saved.getRecipe().getId(),
                saved.getRecipe().getTitle(),
                saved.getRecipe().getImageUrl()
        );

    }

    @Override
    public List<ResponseFavoriteDto> getFavoriteByUser(Long userId) {
        List<Favorite> favorites = favoriteRepository.findByUserId(userId);
        return favorites.stream()
                .map(fav -> new ResponseFavoriteDto(
                    fav.getId(),
                    fav.getUser().getId(),
                    fav.getUser().getUsername(),
                    fav.getRecipe().getId(),
                    fav.getRecipe().getTitle(),
                    fav.getRecipe().getImageUrl()))
                .collect(Collectors.toList());
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
