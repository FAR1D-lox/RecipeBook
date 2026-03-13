package ru.urfu.RecipeBook.recipeLikes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.urfu.RecipeBook.recipeLikes.entity.Like;

public interface LikeRepository extends JpaRepository<Like, Long> {
    boolean existsByUserIdAndRecipeId(Long userId, Long recipeId);

    void deleteByUserIdAndRecipeId(Long userId, Long recipeId);

    long countByRecipeId(Long recipeId);
}
