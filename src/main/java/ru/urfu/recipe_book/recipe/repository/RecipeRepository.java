package ru.urfu.recipe_book.recipe.repository;

import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.urfu.recipe_book.recipe.entity.Recipe;


import java.util.List;


public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    List<Recipe> findRecipesByAuthorId(Long authorId);

    Recipe findRecipeById(Long id);

    List<Recipe> findByTitleContainingIgnoreCase(String title);

    @Query("""
        SELECT r FROM Recipe r
        WHERE (:cursor IS NULL OR r.id > :cursor)
        ORDER BY r.id ASC
    """)
    List<Recipe> fetchNextPage(@Param("cursor") Long cursor, Limit limit);
}
