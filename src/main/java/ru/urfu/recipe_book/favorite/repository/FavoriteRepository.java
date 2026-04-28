package ru.urfu.recipe_book.favorite.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.urfu.recipe_book.favorite.entity.Favorite;
import ru.urfu.recipe_book.user.entity.User;

import java.util.List;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    List<Favorite> findByUserId(Long userId);
    boolean existsByUserIdAndRecipeId(Long userId, Long recipeId);

    void deleteByUser(User user);
}
