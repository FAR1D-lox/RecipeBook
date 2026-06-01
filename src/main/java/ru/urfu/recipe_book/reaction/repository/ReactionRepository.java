package ru.urfu.recipe_book.reaction.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.urfu.recipe_book.reaction.entity.Reaction;
import ru.urfu.recipe_book.user.entity.User;

import java.util.Optional;


public interface
ReactionRepository extends JpaRepository<Reaction, Long> {
    Optional<Reaction> findByUserAndRecipeId(User user, Long recipeId);

    void deleteByUser(User user);

    @Modifying
    @Query(value = """
            UPDATE recipe r
            SET likes_count = r.likes_count - COALESCE((
                SELECT COUNT(1) FROM likes l
                WHERE l.recipe_id = r.id
                AND l.liked = true
                AND l.user_id = :userId)
            , 0),
            dislikes_count = r.dislikes_count - COALESCE((
                SELECT COUNT(1) FROM likes l
                WHERE l.recipe_id = r.id
                AND l.liked = false
                AND l.user_id = :userId
            ), 0)
            """, nativeQuery = true)
    void updateCountersReactionsForRecipes(@Param("userId") Long userId);
}
