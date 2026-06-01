package ru.urfu.recipe_book.recipe.repository;

import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.urfu.recipe_book.recipe.entity.Recipe;
import ru.urfu.recipe_book.user.entity.User;


import java.util.List;
import java.util.Optional;


public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    List<Recipe> findRecipesByAuthorId(Long authorId);

    Optional<Recipe> findRecipeById(Long id);

    List<Recipe> findByTitleContainingIgnoreCase(String title);

    @Query("""
        SELECT r FROM Recipe r
        WHERE (:cursor IS NULL OR r.id > :cursor)
        ORDER BY r.id ASC
    """)
    List<Recipe> fetchNextPage(@Param("cursor") Long cursor, Limit limit);

    List<Recipe> findTop1000ByOrderByCreatedAtDesc();

    @Modifying
    @Query("UPDATE Recipe r SET r.commentsCount = r.commentsCount + 1 WHERE r.id = :recipeId")
    void incrementCommentsCount(@Param("recipeId") Long recipeId);

    @Modifying
    @Query("UPDATE Recipe r SET r.commentsCount = r.commentsCount - 1 WHERE r.id = :recipeId")
    void decrementCommentsCount(@Param("recipeId") Long recipeId);

    @Modifying
    @Query("UPDATE Recipe r SET r.likesCount = r.likesCount + 1 WHERE r.id = :recipeId")
    void incrementLikesCount(@Param("recipeId") Long recipeId);

    @Modifying
    @Query("UPDATE Recipe r SET r.likesCount = r.likesCount - 1 WHERE r.id = :recipeId")
    void decrementLikesCount(@Param("recipeId") Long recipeId);

    @Modifying
    @Query("UPDATE Recipe r SET r.dislikesCount = r.dislikesCount + 1 WHERE r.id = :recipeId")
    void incrementDislikesCount(@Param("recipeId") Long recipeId);

    @Modifying
    @Query("UPDATE Recipe r SET r.dislikesCount = r.dislikesCount - 1 WHERE r.id = :recipeId")
    void decrementDislikesCount(@Param("recipeId") Long recipeId);


    void deleteByAuthor(User user);
}
