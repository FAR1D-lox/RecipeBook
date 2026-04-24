package ru.urfu.recipe_book.reaction.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.urfu.recipe_book.reaction.entity.Reaction;
import java.util.Optional;


public interface
ReactionRepository extends JpaRepository<Reaction, Long> {
    Optional<Reaction> findByUserIdAndRecipeId(Long userId, Long recipeId);

    @Query ("SELECT COUNT(1) FROM Reaction l WHERE l.recipe.id = :recipeId AND l.liked = :liked")
    Long countByRecipeIdAndLiked(@Param("recipeId") Long recipeId, @Param("liked") Boolean liked);
}
