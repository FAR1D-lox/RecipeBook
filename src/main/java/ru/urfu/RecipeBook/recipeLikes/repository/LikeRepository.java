package ru.urfu.RecipeBook.recipeLikes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.urfu.RecipeBook.recipeLikes.entity.Like;
import java.util.Optional;


public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByUserIdAndRecipeId(Long userId, Long recipeId);

    @Query ("SELECT COUNT(l) FROM Like l WHERE l.recipe.id = :recipeId AND l.isLike = :isLike")
    Long countByRecipeIdAndIsLike(@Param("recipeId") Long recipeId, @Param("isLike") Boolean isLike);
}
