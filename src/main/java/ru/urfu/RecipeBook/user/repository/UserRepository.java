package ru.urfu.RecipeBook.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.urfu.RecipeBook.user.entity.User;

import java.util.List;
import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {
    void deleteById(Long userId);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
    List<User> findByUsernameContainingIgnoreCase(String username);

}
