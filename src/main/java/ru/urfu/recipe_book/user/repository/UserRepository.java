package ru.urfu.recipe_book.user.repository;

import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.urfu.recipe_book.user.entity.User;

import java.util.List;
import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {
    void deleteById(Long userId);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
    Optional<User> findByEmail(String email);

    @Query("""
            SELECT u FROM User as u
            WHERE u.username LIKE %:username%
            AND (:cursor IS NULL OR u.id > :cursor)
            ORDER BY u.id
            """)
    List<User> fetchNextPageFiltered(@Param("cursor") Long cursor,
                                     @Param("username") String username,
                                     Limit limit);
}
