package ru.urfu.recipe_book.favorite.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.urfu.recipe_book.favorite.entity.Favorite;

import java.util.List;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    List<Favorite> findByUserId(Long userId);
    boolean existsByUserIdAndRecipeId(Long userId, Long recipeId);
}
