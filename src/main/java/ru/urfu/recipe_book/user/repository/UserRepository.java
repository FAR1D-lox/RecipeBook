package ru.urfu.recipe_book.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.urfu.recipe_book.user.entity.User;

import java.util.List;


public interface UserRepository extends JpaRepository<User, Long> {
    void deleteById(Long userId);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
    List<User> findByUsernameContainingIgnoreCase(String username);

}
