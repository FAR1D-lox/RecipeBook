package ru.urfu.RecipeBook.recipeLikes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.urfu.RecipeBook.recipeLikes.entity.Like;
import java.util.Optional;


public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByUserIdAndRecipeId(Long userId, Long recipeId);

    void deleteByUserIdAndRecipeId(Long userId, Long recipeId);

    long countByRecipeId(Long recipeId);

    long countByRecipeIdAndIsLikeTrue(Long recipeId);
}
